package it.unisa.backend.controller;

import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.InvoiceBean;
import it.unisa.backend.model.bean.OrderBean;
import it.unisa.backend.model.bean.OrderItemBean;
import it.unisa.backend.model.bean.PaymentBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.bean.util.OrderStatus;
import it.unisa.backend.model.bean.util.PaymentStatus;
import it.unisa.backend.model.dao.impl.CartDAO;
import it.unisa.backend.model.dao.impl.OrderDAO;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ConfirmOrderServlet")
public class ConfirmOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDAO orderDao;
    private CartDAO cartDao;
    private ProductDAO productDao;

    @Override
    public void init() throws ServletException {
        orderDao = new OrderDAO(DBManager.getDataSource());
        cartDao = new CartDAO(DBManager.getDataSource());
        productDao = new ProductDAO(DBManager.getDataSource());
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String result = request.getParameter("result");
        if("success".equals(result)) {
            request.setAttribute("successMessage", "Ordine Effettuato Con Successo");
            request.getRequestDispatcher("/WEB-INF/view/success.jsp").forward(request, response);
            return;
        }
        if("failed".equals(result)) {
            String reason = request.getParameter("reason");
            if ("stock".equals(reason)) {
                request.setAttribute("errorMessage", "Siamo spiacenti, alcuni prodotti nel tuo carrello sono esauriti.");
            } else {
                request.setAttribute("errorMessage", "L'ordine non è stato effettuato per un errore di sistema.");
            }
            request.getRequestDispatcher("/WEB-INF/view/failed.jsp").forward(request, response);
            return;
        }
        
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
       request.setCharacterEncoding("UTF-8");
       
       OrderBean pendingOrder = (OrderBean) request.getSession().getAttribute("pendingOrder");
       UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
       
       if (loggedUser == null || pendingOrder == null) {
           response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
           return;
       }
       
       String paymentMethod = request.getParameter("paymentMethod");
       if(paymentMethod == null || paymentMethod.isEmpty()) {
           response.sendRedirect(request.getContextPath() + "/PaymentServlet");
           return;
       }
       
       String cardCircuit = null;
       String lastFourDigits = null;
       
       if("CreditCard".equals(paymentMethod.trim())) {
           String cardNumber = request.getParameter("cardNumber");
           if (cardNumber != null && !cardNumber.isEmpty()) {
               String cleanNumber = cardNumber.replaceAll("\\s+", "");
               
               if (cleanNumber.length() >= 4) {
                   lastFourDigits = cleanNumber.substring(cleanNumber.length() - 4);
               }
               if (cleanNumber.startsWith("4")) {
                   cardCircuit = "Visa";
               } else if (cleanNumber.matches("^5[1-5].*") || cleanNumber.matches("^2[2-7].*")) {
                   cardCircuit = "MasterCard";
               } else if (cleanNumber.matches("^3[47].*")) {
                   cardCircuit = "AmericanExpress";
               } else {
                   cardCircuit = "Generic"; 
               }
           }
       // This section is simulated because there is no payment provider request
       } else if ("PayPal".equals(paymentMethod.trim())) {
           cardCircuit = "PayPal";
           lastFourDigits = "1234";
       } else if ("ApplePay".equals(paymentMethod.trim())) {
           cardCircuit = "ApplePay";
           lastFourDigits = "1234";
       }
       
       // Prepare Payment
       PaymentBean payment = new PaymentBean();
       payment.setPaymentDate(LocalDateTime.now());
       payment.setPaymentMethod(paymentMethod.trim());
       payment.setStatus(PaymentStatus.COMPLETED);
       payment.setTransactionId(UUID.randomUUID().toString());
       payment.setTotalPrice(pendingOrder.getTotalAmount());
       payment.setCardCircuit(cardCircuit);
       payment.setLastFourDigits(lastFourDigits);
        
       pendingOrder.setPayment(payment);
       pendingOrder.setStatus(OrderStatus.PROCESSING);
       
       // Prepare Invoice
       InvoiceBean invoice = new InvoiceBean();
       invoice.setNumber("FATT-" + java.time.Year.now().getValue() + "-" + System.currentTimeMillis());
       invoice.setIssueDate(LocalDateTime.now());
       invoice.setHolderFirstName(loggedUser.getFirstName());
       invoice.setHolderLastName(loggedUser.getLastName());       
       invoice.setBillingAddress(pendingOrder.getShippingAddress());
       
       double totalGross = 0.0;
       double totalTaxable = 0.0;

       for (OrderItemBean item : pendingOrder.getItems()) {
           double priceGross = item.getPriceAtPurchase(); 
           int quantity = item.getQuantity();
           double vatRate = item.getVat();              
           
           double lineGross = priceGross * quantity;
           
           double vatDivider = 1 + (vatRate / 100.0);
           double lineTaxable = lineGross / vatDivider;
           
           totalGross += lineGross;
           totalTaxable += lineTaxable;
       }

       totalGross = Math.round(totalGross * 100.0) / 100.0;
       totalTaxable = Math.round(totalTaxable * 100.0) / 100.0;

       invoice.setTotalAmount(totalGross);
       invoice.setTaxableAmount(totalTaxable);
       
       pendingOrder.setInvoice(invoice);
       
       boolean allStockUpdated = true;

       for (OrderItemBean item : pendingOrder.getItems()) {
           long variantId = item.getVariant().getId(); 
           int qtyToBuy = item.getQuantity();
           

           boolean stockUpdated = productDao.decreaseVariantQuantity(variantId, qtyToBuy);
           
           if (!stockUpdated) {
               allStockUpdated = false;
               break; 
           }
       }
       
       if (!allStockUpdated) {
           response.sendRedirect(request.getContextPath() + "/ConfirmOrderServlet?result=failed&reason=stock");
           return;
       }


       if (orderDao.save(pendingOrder)) {
           
           request.getSession().setAttribute("lastOrderId", pendingOrder.getId());
           request.getSession().removeAttribute("pendingOrder");
           
           CartBean cart = cartDao.findByUserEmail(loggedUser.getEmail());
           if (cart != null) {
               cartDao.clearCart(cart.getId());
           }
           
           response.sendRedirect(request.getContextPath() + "/ConfirmOrderServlet?result=success");
       } else {
           response.sendRedirect(request.getContextPath() + "/ConfirmOrderServlet?result=failed");
       }
       
    }
}