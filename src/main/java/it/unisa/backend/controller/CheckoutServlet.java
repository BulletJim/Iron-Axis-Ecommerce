package it.unisa.backend.controller;

import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.UserBean;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        
        HttpSession session = request.getSession();
        
        UserBean user = (UserBean) session.getAttribute("loggedUser");

        if (user == null) {                                                          //Controllo autenticazione utente
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            
            return;
        }

        CartBean cart = (CartBean) session.getAttribute("cart");
        
        if (cart == null || cart.getVariants() == null || cart.getVariants().isEmpty()) {          //Controllo presenza e contenuto carrello 
            response.sendRedirect(request.getContextPath() + "/CartServlet");
            
            return;
        }

        LocalDate deliveryDate = LocalDate.now();     //Calcolo della data di consegna 
        int workingDaysToAdd = 3;
        int addedDays = 0;

        while (addedDays < workingDaysToAdd) {
            deliveryDate = deliveryDate.plusDays(1);
            
            if (deliveryDate.getDayOfWeek() != DayOfWeek.SATURDAY && deliveryDate.getDayOfWeek() != DayOfWeek.SUNDAY) {       // Controlla se il giorno calcolato è un giorno feriale    
                addedDays++;
            }
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.ITALY);
        String formattedDeliveryDate = deliveryDate.format(formatter);

        formattedDeliveryDate = formattedDeliveryDate.substring(0, 1).toUpperCase() + formattedDeliveryDate.substring(1);

        request.setAttribute("estimatedDelivery", formattedDeliveryDate);
        request.setAttribute("totalPrice", cart.getTotalPrice());
        
        request.getRequestDispatcher("/WEB-INF/view/order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
