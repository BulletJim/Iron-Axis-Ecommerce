package it.unisa.backend.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.util.PasswordUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		LocalDate dob = LocalDate.parse(request.getParameter("dateOfBirth"));
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		// Server-side email and password check
		if(email == null || email.trim().isEmpty() || password == null || email.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Tutti i campi sono obbligatori");
			request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
			return;
		}
		
		String salt = PasswordUtil.generateSalt();
		String hashedPassword = PasswordUtil.hashPassword(password, salt);
		
		//Create UserBean and call Dao
		UserBean user = new UserBean();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email.trim());
		user.setPasswordHash(hashedPassword);
		user.setPasswordSalt(salt);
		user.setBirthDate(dob);
		user.setRole("user");
		user.setRegistrationDate(LocalDateTime.now());
		
	}

}
