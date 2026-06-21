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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet("/admin/AdminOrderServlet")
public class AdminOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String customerQuery = request.getParameter("customerQuery");

        String sqlStartDate = convertDateToSqlFormat(startDateStr);
        String sqlEndDate = convertDateToSqlFormat(endDateStr);

        OrderDAO orderDao = new OrderDAO(DBManager.getDataSource());
        List<OrderBean> orders;

        boolean hasFilters = (sqlStartDate != null) || 
                             (sqlEndDate != null) || 
                             (customerQuery != null && !customerQuery.trim().isEmpty());

        if (hasFilters) {
            orders = orderDao.findOrdersByFilters(sqlStartDate, sqlEndDate, customerQuery);
        } 
        else {
            orders = orderDao.findAll();
        }

        ProductDAO productDao = new ProductDAO(DBManager.getDataSource());
        List<ProductBean> products = productDao.findAll();

        request.setAttribute("orders", orders);
        request.setAttribute("products", products); 
        request.setAttribute("activePanel", "panel-orders");
        
        request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
    }

    private String convertDateToSqlFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateStr, inputFormatter);
            return date.toString(); 
        } catch (DateTimeParseException e) {
            System.err.println("Errore di parsing della data: " + dateStr);
            return null; 
        }
    }
}