package it.unisa.backend.controller;

import java.io.IOException;
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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		if(email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Inserire Email e Password");
			doGet(request, response);
			return;
		}
		
		//Call UserDAO and check user existence in db
		UserDAO userDao = new UserDAO(DBManager.getDataSource());
		
		email = email.trim();
		password = password.trim();
		
		try {
			
			UserBean user = userDao.findByEmail(email);
			
			if(user != null) {
				String storedHashedPassword = user.getPasswordHash();
				String storedSalt = user.getPasswordSalt();
				
				 if(PasswordUtil.verifyPassword(password, storedHashedPassword, storedSalt)) {
					 //User Verified
					 HttpSession session = request.getSession();
					 session.setAttribute("loggedUser", user);
					 response.sendRedirect(request.getContextPath() + "/index.jsp");
					 return;
				 }
				
			}
			
			request.setAttribute("errorMessage", "Email o password errate");
			doGet(request, response);
			
		} catch (Exception e) {
			throw new ServletException("ERROR 500 INTERNAL SERVER ERROR");
		}
		
	}

}
