package it.unisa.backend.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.AddressBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.UserDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/AddressServlet")
public class AddressServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private UserDAO userDao;
	
	
	@Override
	public void init() throws ServletException {
		userDao = new UserDAO(DBManager.getDataSource());
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
		
		String action = request.getParameter("action");
		
		if("add".equals(action)) {
			
			String street = request.getParameter("street");
			String streetNumber = request.getParameter("streetNumber");
			String city = request.getParameter("city");
			String zipCode = request.getParameter("zipCode");
			String province = request.getParameter("province");
			String country = request.getParameter("country");
			if(street == null || streetNumber == null || city == null 
					|| zipCode == null || province == null || country == null) {
				response.getWriter().write("Errore: tutti i campi devono essere compilati");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
			}
			
			UserBean loggedUser = (UserBean)request.getSession().getAttribute("loggedUser");
			if(loggedUser == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User should be logged");
				return;
			}
			
			AddressBean address = new AddressBean();
			address.setUserEmail(loggedUser.getEmail());
			address.setZipCode(zipCode.trim());
			address.setCity(city.trim());
			address.setStreet(street.trim());
			address.setStreetNumber(Integer.parseInt(streetNumber.trim()));
			address.setProvince(province.trim());
			address.setCountry(country.trim());
			
			if(userDao.addAddressToUserByEmail(loggedUser.getEmail(), address)) {		
				response.setStatus(HttpServletResponse.SC_OK);
				request.getSession().setAttribute("successMessage", "Indirizzo salvato correttamente");
			} else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Errore: Impossibile salvare l'indirizzo nel database.");
            }
			
			
		} else {
			response.getWriter().write("Errore: indirizzo non registrato correttamente");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
		}
	}

}
