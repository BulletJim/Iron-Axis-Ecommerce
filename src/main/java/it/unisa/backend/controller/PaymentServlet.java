package it.unisa.backend.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.AddressBean;
import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.CartItemBean;
import it.unisa.backend.model.bean.OrderBean;
import it.unisa.backend.model.bean.OrderItemBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.bean.util.OrderStatus;
import it.unisa.backend.model.dao.impl.CartDAO;
import it.unisa.backend.model.dao.impl.UserDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CartDAO cartDao;
    private UserDAO userDao;
    
    
    @Override
	public void init() throws ServletException {
    	cartDao = new CartDAO(DBManager.getDataSource());
    	userDao = new UserDAO(DBManager.getDataSource());
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("pendingOrder") == null) {
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
        
        request.getRequestDispatcher("/WEB-INF/view/payment.jsp").forward(request, response);
		
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String addressIdParam = request.getParameter("selectedAddressId");
    	long addressId = Long.parseLong(addressIdParam);
    	
    	 final UserBean loggedUser = (UserBean)request.getSession().getAttribute("loggedUser");
    	final CartBean cart = cartDao.findByUserEmail(loggedUser.getEmail());
    	if (loggedUser == null || cart == null || cart.getVariants().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
            return;
        }
    	
    	// Find the address in DB
    	AddressBean address = userDao.findAddressById(addressId);
    	
    	// Constructing the order
    	OrderBean order = new OrderBean();
    	order.setUser(loggedUser);
    	order.setShippingAddress(address);
    	order.setStatus(OrderStatus.PENDING);
    	order.setCreatedAt(LocalDateTime.now());
    	
    	// Add Shipping costs if total is below 50
    	double cartSubtotal = cart.getTotalPrice();
    	double shippingCosts = (cartSubtotal >= 50 || cartSubtotal == 0) ? 0.0 : 5.90;
    	
    	order.setTotalAmount(cartSubtotal + shippingCosts);
    	
    	// Build OrderItem
    	final Map<Long, CartItemBean> cartItems = cart.getVariants();
    	List<OrderItemBean> orderItems = new ArrayList<>();
    	
    	cartItems.forEach((key, value) ->{
    		OrderItemBean orderItem = new OrderItemBean();
    		orderItem.setPriceAtPurchase(value.getVariant().getPrice());
    		orderItem.setQuantity(value.getSelectedQuantity());
    		orderItem.setVariant(value.getVariant());
    		orderItem.setVat(value.getVariant().getVat());
    		orderItems.add(orderItem);
    	});
    	
    	order.setItems(orderItems);
    	
    	// Ignore invoice and payment as they will be added in payment area and confirmation area
    	
    	request.getSession().setAttribute("pendingOrder", order);
    	response.sendRedirect(request.getContextPath() + "/PaymentServlet");
    	
    	
    	
    	
    }
    

}
