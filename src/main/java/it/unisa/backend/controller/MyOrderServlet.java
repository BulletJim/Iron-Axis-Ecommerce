package it.unisa.backend.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.backend.model.bean.OrderBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.OrderDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/MyOrderServlet")
public class MyOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserBean loggedUser = (UserBean) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        OrderDAO orderDao = new OrderDAO(DBManager.getDataSource());
        List<OrderBean> orders = orderDao.findByUserEmail(loggedUser.getEmail());

        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/view/myorders.jsp").forward(request, response);
    }
}