package it.unisa.backend.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebServlet("/CheckEmailServlet")
public class CheckEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String emailInput = request.getParameter("email");
		boolean emailExists = false;
		
		if(emailInput != null && emailInput.trim().equalsIgnoreCase("test@test.com")) {
			emailExists = true;
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Map<String, Object> resData = new HashMap<>();
		resData.put("emailExists", emailExists);
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.writeValue(response.getWriter(), resData);
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/error/error403.jsp").forward(request, response);
	}

}
