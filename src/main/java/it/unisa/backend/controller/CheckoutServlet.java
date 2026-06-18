package it.unisa.backend.controller;

import it.unisa.backend.model.bean.AddressBean;
import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.CartDAO;
import it.unisa.backend.model.dao.impl.UserDAO;
import it.unisa.backend.model.db.DBManager;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    private CartDAO cartDao;
    private UserDAO userDao;
    
    @Override
	public void init() throws ServletException {
    	 cartDao = new CartDAO(DBManager.getDataSource());
    	 userDao = new UserDAO(DBManager.getDataSource());
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
    	UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
    	try {
			if(loggedUser != null) {
				CartBean cart = cartDao.findByUserEmail(loggedUser.getEmail());
				if(cart == null || cart.getVariants().isEmpty()) {
					throw new Exception("The user has no cart");
				}
				
				List<AddressBean> userAddresses = loggedUser.getAddresses();
				
				// Process Delivery date
				LocalDate deliveryDate = LocalDate.now();     
		        int workingDaysToAdd = 3;
		        int addedDays = 0;

		        while (addedDays < workingDaysToAdd) {
		            deliveryDate = deliveryDate.plusDays(1);
		            
		            // No Delivery in the weekend
		            if (deliveryDate.getDayOfWeek() != DayOfWeek.SATURDAY && deliveryDate.getDayOfWeek() != DayOfWeek.SUNDAY) {         
		                addedDays++;
		            }
		        }
		        
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.ITALY);
		        String formattedDeliveryDate = deliveryDate.format(formatter);

		        formattedDeliveryDate = formattedDeliveryDate.substring(0, 1).toUpperCase() + formattedDeliveryDate.substring(1);

		        // Fetch user addresses by updating the session
		        UserBean updatedUser = userDao.findByEmail(loggedUser.getEmail());
		        List<AddressBean> fetchedAddresses = updatedUser.getAddresses();
		        
		        request.setAttribute("userAddresses", fetchedAddresses);
		        request.setAttribute("estimatedDelivery", formattedDeliveryDate);
		        request.setAttribute("cart", cart);
		        
		        request.getRequestDispatcher("/WEB-INF/view/order.jsp").forward(request, response);
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		}

        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
