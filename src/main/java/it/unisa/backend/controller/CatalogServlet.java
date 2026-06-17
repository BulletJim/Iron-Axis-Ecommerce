package it.unisa.backend.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/CatalogServlet")
public class CatalogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String categoryIdStr = request.getParameter("categoryId");
		String maxPriceStr = request.getParameter("maxPrice");
		String sortBy = request.getParameter("sortBy");
		String onlyAvailableStr = request.getParameter("onlyAvailable");
		String isAjax = request.getParameter("ajax");

		Long categoryId = null;
		if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
			try {
				categoryId = Long.parseLong(categoryIdStr);
			} catch (NumberFormatException e) {
				categoryId = null;
			}
		}

		Double maxPrice = null;
		if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
			try {
				maxPrice = Double.parseDouble(maxPriceStr);
			} catch (NumberFormatException e) {
				maxPrice = null;
			}
		}
		
		boolean onlyAvailable = "true".equals(onlyAvailableStr);

		ProductDAO productDao = new ProductDAO(DBManager.getDataSource());
		List<ProductBean> products = productDao.getProductsByFilters(categoryId, maxPrice, sortBy, onlyAvailable);
		
		request.setAttribute("products", products);
		request.setAttribute("selectedCategoryId", categoryId);


		if ("true".equals(isAjax)) {
			request.getRequestDispatcher("/WEB-INF/fragment/productGridSnippet.jsp").forward(request, response);
		} else {
			List<ProductBean> topRated = productDao.doRetrieveTopRated(5);
	        request.setAttribute("topRated", topRated);
	     
	        List<ProductBean> suggested = productDao.doRetrieveSuggested(5);
	        request.setAttribute("suggested", suggested);
	        request.getRequestDispatcher("/WEB-INF/view/catalog.jsp").forward(request, response);
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
	}

}
