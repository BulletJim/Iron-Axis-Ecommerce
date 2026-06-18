package it.unisa.backend.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.OrderBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.OrderDAO;
import it.unisa.backend.model.db.DBManager;


@WebServlet("/DownloadInvoiceServlet")
public class DownloadInvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private OrderDAO orderDao;
	
	@Override
	public void init() throws ServletException {
		orderDao = new OrderDAO(DBManager.getDataSource());
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
		if(loggedUser == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User not found");
			return;
		}
		
		Long orderId = (Long) request.getSession().getAttribute("lastOrderId");
		if (orderId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order Id not found");
            return;
        }
		
		OrderBean order = orderDao.findById(orderId);
		if (order == null || !order.getUser().getEmail().equals(loggedUser.getEmail())) {
	        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden.");
	        return;
	    }
		
		try {
			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=fattura_" + order.getInvoice().getNumber() + ".pdf");
			
			// TODO: Implement the pdf generation logic using OpenPDF
			
			
		} catch(Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Pdf Generation Failed");
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
	}

}
