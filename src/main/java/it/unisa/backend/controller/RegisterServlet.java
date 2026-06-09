package it.unisa.backend.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.AddressBean;
import it.unisa.backend.model.bean.PhoneBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.bean.util.PhoneType;
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
				String type = phoneTypes[i];
				String number = phoneNumbers[i];
				
				if(number != null && !number.trim().isEmpty()) {
					PhoneBean phone = new PhoneBean();
					phone.setPhoneNumber(number);
					phone.setUserEmail(email);
					PhoneType phoneType = PhoneType.valueOf(type.toUpperCase().trim());
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
				address.setStreet(streets[i]);
				address.setUserEmail(email);
				address.setStreetNumber(Integer.parseInt(streetNumbers[i]));
				address.setCity(cities[i]);
				address.setProvince(provs[i]);
				address.setZipCode(zipCodes[i]);
				address.setCountry(countries[i]);
				
				addresses.add(address);
			}
		}
		
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
		user.setAddresses(addresses);
		user.setPhones(phones);
		
	}

}
