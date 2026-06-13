package it.unisa.backend.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.AddressBean;
import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.PhoneBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.bean.util.PhoneType;
import it.unisa.backend.model.dao.impl.CartDAO;
import it.unisa.backend.model.dao.impl.UserDAO;
import it.unisa.backend.model.db.DBManager;
import it.unisa.backend.util.PasswordUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
		
		
		//Phone numbers validation
		String[] phoneTypes = request.getParameterValues("phoneType");
		String[] phoneNumbers = request.getParameterValues("phoneNumber");
		
		List<PhoneBean> phones = new ArrayList<>();
		
		if(phoneTypes != null && phoneNumbers != null) {
			for(int i = 0; i < phoneNumbers.length; i++) {
				String type = phoneTypes[i].trim();
				String number = phoneNumbers[i].trim();
				
				if(number != null && !number.isEmpty()) {
					PhoneBean phone = new PhoneBean();
					phone.setPhoneNumber(number);
					phone.setUserEmail(email);
					PhoneType phoneType = PhoneType.valueOf(type.toUpperCase());
					phone.setType(phoneType);
					
					phones.add(phone);
				}
			}
		}
		
		// Address validation
		String[] streets = request.getParameterValues("street");
		String[] streetNumbers = request.getParameterValues("streetNumber");
		String[] cities = request.getParameterValues("city");
		String[] provs = request.getParameterValues("prov");
		String[] zipCodes = request.getParameterValues("zipCode");
		String[] countries = request.getParameterValues("country");
		
		List<AddressBean> addresses = new ArrayList<>();
		
		if(streets != null) {
			for(int i = 0; i < streets.length; i++) {
				AddressBean address = new AddressBean();
				address.setStreet(streets[i].trim());
				address.setUserEmail(email);
				address.setStreetNumber(Integer.parseInt(streetNumbers[i].trim()));
				address.setCity(cities[i].trim());
				address.setProvince(provs[i].trim());
				address.setZipCode(zipCodes[i].trim());
				address.setCountry(countries[i].trim());
				
				addresses.add(address);
			}
		}
		
		// Server-side email and password check
		if(email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Tutti i campi sono obbligatori");
			request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
			return;
		}
		
		String salt = PasswordUtil.generateSalt();
		String hashedPassword = PasswordUtil.hashPassword(password.trim(), salt);
		
		//Create UserBean and call Dao
		UserBean user = new UserBean();
		user.setFirstName(firstName.trim());
		user.setLastName(lastName.trim());
		user.setEmail(email.trim());
		user.setPasswordHash(hashedPassword);
		user.setPasswordSalt(salt);
		user.setBirthDate(dob);
		user.setRole("user");
		user.setRegistrationDate(LocalDateTime.now());
		user.setAddresses(addresses);
		user.setPhones(phones);
		
		// Call to UserDAO and CartDAO
		UserDAO userDao = new UserDAO(DBManager.getDataSource());
		CartDAO cartDao = new CartDAO(DBManager.getDataSource());
		
		// Create user and then create the user cart
		try {
			if(userDao.save(user)) {
				CartBean cart = new CartBean();
				cart.setUserEmail(email.trim());
				cart.setTotalPrice(0.0);
				cart.setCreationDate(LocalDateTime.now());
				
				if(cartDao.save(cart)) {
					request.getSession().setAttribute("successMessage", "Registrazione completata con successo!");
					response.setStatus(HttpServletResponse.SC_CREATED);
			        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
			        
				} else {
					System.err.println("Errore: cart related to user " + user.getEmail() + " not initialized");
					request.setAttribute("errorMessage", "Errore nella creazione del carrello");
					request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("errorMessage", "Errore nella creazione dell'utente");
				request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("ERROR 500 INTERNAL SERVER ERROR", e);
		}

	}
}
