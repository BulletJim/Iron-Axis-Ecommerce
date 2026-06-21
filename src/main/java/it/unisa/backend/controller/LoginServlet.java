package it.unisa.backend.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.CartItemBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.bean.VariantBean;
import it.unisa.backend.model.bean.dto.GuestCartItemDTO;
import it.unisa.backend.model.dao.impl.CartDAO;
import it.unisa.backend.model.dao.impl.ProductDAO;
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
					 
					 // Cart Merging if guest_cart exists
					 mergeGuestCart(request, response, user);
					 
					 session.setAttribute("successMessage", "Utente loggato con successo");
					 response.sendRedirect(request.getContextPath() + "/index.jsp");
					 return;
				 }
				
			}
			
			request.getSession().setAttribute("errorMessage", "Email o password errate");
			doGet(request, response);
			
		} catch (Exception e) {
			throw new ServletException("ERROR 500 INTERNAL SERVER ERROR");
		}
		
	}
	
	private void mergeGuestCart(HttpServletRequest request, HttpServletResponse response, UserBean loggedUser) throws IOException {
		Cookie[] cookies = request.getCookies();
		String guestCartJson = null;
		Cookie guestCookieToDestroy = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("guest_cart".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
					guestCartJson = cookie.getValue();
					guestCookieToDestroy = cookie;
					break;
				}
			}
		}

		if (guestCartJson != null) {
			try {
				String decodedJson = URLDecoder.decode(guestCartJson, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				List<GuestCartItemDTO> guestItems = mapper.readValue(decodedJson, new TypeReference<List<GuestCartItemDTO>>(){});

				if (!guestItems.isEmpty()) {
					CartDAO cartDao = new CartDAO(DBManager.getDataSource());
					ProductDAO productDao = new ProductDAO(DBManager.getDataSource());

					CartBean userCart = cartDao.findByUserEmail(loggedUser.getEmail());
					if (userCart == null) {
						userCart = new CartBean();
						userCart.setUserEmail(loggedUser.getEmail());
						userCart.setTotalPrice(0.0);
						userCart.setVariants(new HashMap<>());
						cartDao.save(userCart);
						userCart = cartDao.findByUserEmail(loggedUser.getEmail());
					}

					Map<Long, CartItemBean> userVariants = userCart.getVariants();

					for (GuestCartItemDTO guestItem : guestItems) {
						VariantBean variant = productDao.findVariantBySku(guestItem.getSku());
						if (variant != null) {
							if (userVariants.containsKey(variant.getId())) {

								CartItemBean existingItem = userVariants.get(variant.getId());
								existingItem.setSelectedQuantity(existingItem.getSelectedQuantity() + guestItem.getQuantity());
							} else {

								userVariants.put(variant.getId(), new CartItemBean(variant, guestItem.getQuantity()));
							}
						}
					}


					double total = 0.0;
					for (CartItemBean item : userVariants.values()) {
						total += item.getVariant().getPrice() * item.getSelectedQuantity();
					}
					userCart.setTotalPrice(total);
					cartDao.update(userCart);
				}
				guestCookieToDestroy.setValue("");
				guestCookieToDestroy.setPath("/iron-axis");
				guestCookieToDestroy.setMaxAge(0);
				response.addCookie(guestCookieToDestroy);

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cart merging error");
			}
		}
	}

}
