package it.unisa.backend.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.UserBean;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        UserBean user = (UserBean) session.getAttribute("loggedUser");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            
            return;
        }


        CartBean cart = (CartBean) session.getAttribute("cart");
        
        if (cart == null || cart.getVariants() == null || cart.getVariants().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/CartServlet");
            
            return;
        }

        request.getRequestDispatcher("/WEB-INF/view/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
