package it.unisa.backend.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.UserDAO;
import it.unisa.backend.model.db.DBManager;
import it.unisa.backend.util.PasswordUtil;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {

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

        UserDAO userDao = new UserDAO(DBManager.getDataSource());
        UserBean freshUser = userDao.findByEmail(loggedUser.getEmail());

        request.setAttribute("user", freshUser);
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserBean loggedUser = (UserBean) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthDateStr = request.getParameter("birthDate");
        String newPassword = request.getParameter("newPassword");


        if (firstName == null || firstName.trim().isEmpty()
                || lastName == null || lastName.trim().isEmpty()) {
        	
            request.getSession().setAttribute("errorMessage", "Nome e Cognome sono obbligatori");
            request.setAttribute("user", loggedUser);
            request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
            return;
        }

        UserDAO userDao = new UserDAO(DBManager.getDataSource());
        UserBean userToUpdate = userDao.findByEmail(loggedUser.getEmail());

        userToUpdate.setFirstName(firstName.trim());
        userToUpdate.setLastName(lastName.trim());

        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            String dateToParse = birthDateStr.trim();
            LocalDate parsedDate = null;

            try {
                parsedDate = LocalDate.parse(dateToParse);
            } 
            catch (DateTimeParseException e1) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    parsedDate = LocalDate.parse(dateToParse, formatter);
                } catch (DateTimeParseException e2) {
       
                    request.getSession().setAttribute("errorMessage", "Formato data di nascita non valido. Usa GG/MM/AAAA");
                    request.setAttribute("user", loggedUser);
                    request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
                    return;
                }
            }
            
            if (parsedDate != null) {
                userToUpdate.setBirthDate(parsedDate);
            }
        }

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String newSalt = PasswordUtil.generateSalt();
            String newHash = PasswordUtil.hashPassword(newPassword.trim(), newSalt);
            userToUpdate.setPasswordHash(newHash);
            userToUpdate.setPasswordSalt(newSalt);
        }

        boolean success = userDao.update(userToUpdate);

        if (success) {
         
            session.setAttribute("loggedUser", userToUpdate);
            request.getSession().setAttribute("successMessage", "Profilo aggiornato con successo");
        } 
        else {
            request.getSession().setAttribute("errorMessage", "Errore durante l'aggiornamento del profilo");
        }

        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }
}