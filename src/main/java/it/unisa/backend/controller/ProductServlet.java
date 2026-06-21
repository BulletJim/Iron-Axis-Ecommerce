package it.unisa.backend.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idParam = request.getParameter("id");
		
		if(idParam == null || idParam.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return;
		}
		
		try {
			long id = Long.parseLong(idParam);
			
			ProductDAO productDao = new ProductDAO(DBManager.getDataSource());
			
			ProductBean product = productDao.findById(id);
			
			if(product == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "product with id: " + id + " not found.");
				return;
			}
			
			request.setAttribute("product", product);
			request.getRequestDispatcher("/WEB-INF/view/product.jsp").forward(request, response);
			
		} catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id provided is not a number");
        } catch(Exception e) {
			throw new ServletException("Internal Server Error");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
	}

}
