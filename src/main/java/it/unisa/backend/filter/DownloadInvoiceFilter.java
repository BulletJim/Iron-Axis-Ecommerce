package it.unisa.backend.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.backend.model.bean.OrderBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.OrderDAO;
import it.unisa.backend.model.db.DBManager;


@WebFilter("/DownloadInvoiceServlet")
public class DownloadInvoiceFilter extends HttpFilter implements Filter {
       
    private static final long serialVersionUID = 1L;
	private OrderDAO orderDao;
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		
		// Checks if user is logged
		UserBean loggedUser = session != null ? (UserBean) session.getAttribute("loggedUser") : null;
		if(loggedUser == null) {
			res.sendRedirect(req.getContextPath() + "/LoginServlet");
			return;
		}
		
		String orderIdString = req.getParameter("orderId");
		if(orderIdString == null || orderIdString.trim().isEmpty()) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "No order id selected");
			return;
		}
		
		try {
			
			Long orderId = Long.parseLong(orderIdString);
			
			OrderBean order = orderDao.findById(orderId);
			if(order == null) {
				res.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
				return;
			}
			
			boolean isOwner = loggedUser.getEmail().equals(order.getUser().getEmail());
			boolean isAdmin = "admin".equals(loggedUser.getRole());
			
			if(!isOwner && !isAdmin) {
				res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
				return;
			}
			
			req.setAttribute("authorizedOrder", order);
			chain.doFilter(request, response);
			
		} catch(NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Order Id");
		} catch(Exception e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
		

	}


	public void init(FilterConfig fConfig) throws ServletException {
		orderDao = new OrderDAO(DBManager.getDataSource());
	}

}
