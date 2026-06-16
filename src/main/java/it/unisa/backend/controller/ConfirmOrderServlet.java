package it.unisa.backend.controller;

import it.unisa.backend.model.bean.*;
import it.unisa.backend.model.bean.util.OrderStatus;
import it.unisa.backend.model.bean.util.PaymentStatus;
import it.unisa.backend.model.dao.impl.OrderDAO;
import it.unisa.backend.model.db.DBManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ConfirmOrderServlet")
public class ConfirmOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("loggedUser");
        CartBean cart = (CartBean) session.getAttribute("cart");

        if (user == null || cart == null || cart.getVariants() == null || cart.getVariants().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/CartServlet");
            
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        String cardHolder = request.getParameter("cardHolder");

        OrderBean order = new OrderBean();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalPrice());

        if (user.getAddresses() != null && !user.getAddresses().isEmpty()){
            order.setShippingAddress(user.getAddresses().get(0));
            
        } 
        else {
            AddressBean fallbackAddress = new AddressBean();
            
            fallbackAddress.setId(1L); 
            order.setShippingAddress(fallbackAddress);
        }

        ArrayList<OrderItemBean> orderItems = new ArrayList<>();
        
        for (CartItemBean cartItem : cart.getVariants().values()) {
        	
            OrderItemBean orderItem = new OrderItemBean();
            orderItem.setVariant(cartItem.getVariant());
            orderItem.setQuantity(cartItem.getSelectedQuantity());
            
            orderItem.setPriceAtPurchase(cartItem.getVariant().getPrice());
            orderItem.setVat(cartItem.getVariant().getVat());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);

        PaymentBean payment = new PaymentBean();
        
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : "Carta di credito");
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setTotalPrice(cart.getTotalPrice());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);
        order.setPayment(payment);

        InvoiceBean invoice = new InvoiceBean();
        invoice.setNumber("INV-" + System.currentTimeMillis());
        
        if (cardHolder != null && cardHolder.contains(" ")) {
        	
            String[] parts = cardHolder.split(" ", 2);
            invoice.setHolderFirstName(parts[0]);
            invoice.setHolderLastName(parts[1]);
        } 
        else {
            invoice.setHolderFirstName(user.getFirstName());
            invoice.setHolderLastName(user.getLastName());
        }
        
        invoice.setBillingAddress(order.getShippingAddress());
        invoice.setTotalAmount(cart.getTotalPrice());
        invoice.setTaxableAmount(cart.getTotalPrice() / 1.22); 
        order.setInvoice(invoice);
        
        OrderDAO orderDAO = new OrderDAO(DBManager.getDataSource());
        boolean success = orderDAO.save(order);

        if (success) {
            session.removeAttribute("cart"); 

            String msg = "Pagamento completato con successo! Il tuo numero d'ordine è: <strong>" + invoice.getNumber() + "</strong>";
            session.setAttribute("successMessage", msg);
            response.sendRedirect(request.getContextPath() + "/SuccessServlet");
            
            return;
        }
        else {
            request.getRequestDispatcher("/error/error500.jsp").forward(request, response);
        }
        
    }
    
}
