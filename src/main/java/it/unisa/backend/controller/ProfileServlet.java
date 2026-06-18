package it.unisa.backend.controller;

import java.io.IOException;
import java.time.LocalDate;

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

        // Controllo che l'utente sia loggato
        HttpSession session = request.getSession();
        UserBean loggedUser = (UserBean) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Recupero i dati aggiornati dal database (non uso solo quelli in sessione,
        //    perché potrebbero essere vecchi se sono stati modificati altrove)
        UserDAO userDao = new UserDAO(DBManager.getDataSource());
        UserBean freshUser = userDao.findByEmail(loggedUser.getEmail());

        // Passo i dati alla JSP tramite request attribute
        request.setAttribute("user", freshUser);
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controllo che l'utente sia loggato
        HttpSession session = request.getSession();
        UserBean loggedUser = (UserBean) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Leggo i parametri inviati dal form
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String birthDateStr = request.getParameter("birthDate");
        String newPassword = request.getParameter("newPassword");

        // Validazione base dei campi obbligatori
        if (firstName == null || firstName.trim().isEmpty()
                || lastName == null || lastName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Nome e Cognome sono obbligatori");
            request.setAttribute("user", loggedUser);
            request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
            return;
        }

        // Costruisco il bean aggiornato partendo dai dati attuali dell'utente
        //    (così non perdo email, ruolo, ecc. che non sono nel form)
        UserDAO userDao = new UserDAO(DBManager.getDataSource());
        UserBean userToUpdate = userDao.findByEmail(loggedUser.getEmail());

        userToUpdate.setFirstName(firstName.trim());
        userToUpdate.setLastName(lastName.trim());

        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                userToUpdate.setBirthDate(LocalDate.parse(birthDateStr.trim()));
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Formato data di nascita non valido");
                request.setAttribute("user", loggedUser);
                request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
                return;
            }
        }

        // Se l'utente ha inserito una nuova password, la sostituisco (hash + salt nuovi)
        //    Altrimenti lascio quella vecchia, che è già presente in userToUpdate.
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String newSalt = PasswordUtil.generateSalt();
            String newHash = PasswordUtil.hashPassword(newPassword.trim(), newSalt);
            userToUpdate.setPasswordHash(newHash);
            userToUpdate.setPasswordSalt(newSalt);
        }

        // Chiamo l'update sul DAO
        boolean success = userDao.update(userToUpdate);

        if (success) {
            // Aggiorno anche la sessione con i dati nuovi, altrimenti l'header
            //    mostrerebbe ancora "Ciao, [vecchio nome]" finché non si rifà login
            session.setAttribute("loggedUser", userToUpdate);
            request.setAttribute("successMessage", "Profilo aggiornato con successo");
        } else {
            request.setAttribute("errorMessage", "Errore durante l'aggiornamento del profilo");
        }

        request.setAttribute("user", userToUpdate);
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp").forward(request, response);
    }
}