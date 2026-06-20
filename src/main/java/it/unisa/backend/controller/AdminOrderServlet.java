package it.unisa.backend.controller;

import it.unisa.backend.model.bean.OrderBean;
import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.dao.impl.OrderDAO;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/AdminOrderServlet")
public class AdminOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String customerQuery = request.getParameter("customerQuery");

        OrderDAO orderDao = new OrderDAO(DBManager.getDataSource());
        List<OrderBean> orders;

        boolean hasFilters = (startDate != null && !startDate.isEmpty()) || 
                             (endDate != null && !endDate.isEmpty()) || 
                             (customerQuery != null && !customerQuery.isEmpty());

        if (hasFilters) {
            orders = orderDao.findOrdersByFilters(startDate, endDate, customerQuery);
        } 
        else {
            orders = orderDao.findAll();
        }

        ProductDAO productDao = new ProductDAO(DBManager.getDataSource());
        List<ProductBean> products = productDao.findAll();

        request.setAttribute("orders", orders);
        request.setAttribute("products", products); 
        request.setAttribute("activePanel", "panel-orders");
        
        request.getRequestDispatcher("/WEB-INF/view/dashboard.jsp").forward(request, response);
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
	}
}