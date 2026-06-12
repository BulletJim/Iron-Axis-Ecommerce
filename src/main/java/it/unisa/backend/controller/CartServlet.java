package it.unisa.backend.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unisa.backend.model.bean.CartBean;
import it.unisa.backend.model.bean.CartItemBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.bean.VariantBean;
import it.unisa.backend.model.bean.dto.GuestCartItemDTO;
import it.unisa.backend.model.dao.impl.CartDAO;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CartDAO cartDao;
    private ProductDAO productDao;
    
    public void init() throws ServletException {
        cartDao = new CartDAO(DBManager.getDataSource());
        productDao = new ProductDAO(DBManager.getDataSource()); 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		if(action == null) action = "view";
		
		UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
		CartBean cart = null;
		
		try {
			if("view".equals(action)) {
				if(loggedUser != null) {
					// logged user cart
					 cart = cartDao.findByUserEmail(loggedUser.getEmail());
					 if(cart == null) {
						 cart = createEmptyCart(loggedUser.getEmail());
					 }
				} else {
					// guest cart
					cart = getCartFromCookie(request);
				}
			}
			request.setAttribute("cart", cart);
			request.getRequestDispatcher("/WEB-INF/view/cart.jsp").forward(request, response);
			
		} catch(Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String sku = request.getParameter("productSku");
		String action = request.getParameter("action");
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		
		UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
		
		try {

			if ("add".equals(action)) {

				VariantBean variant = productDao.findVariantBySku(sku);

				if (variant != null) {

					if (loggedUser != null) {
						// logged user
						CartBean cart = cartDao.findByUserEmail(loggedUser.getEmail());
						if (cart == null) {
							// no cart associated
							cart = createEmptyCart(loggedUser.getEmail());
							cartDao.save(cart);
							cart = cartDao.findByUserEmail(loggedUser.getEmail());
						}

						Map<Long, CartItemBean> variants = cart.getVariants();
						// if the variant is present in the cart
						if (variants.containsKey(variant.getId())) {
							// increase the quantity selected
							CartItemBean item = variants.get(variant.getId());
							item.setSelectedQuantity(item.getSelectedQuantity() + quantity);
						} else {
							// add the variant to the cart
							variants.put(variant.getId(), new CartItemBean(variant, quantity));
						}

						recalculateAndSetTotal(cart);
						cartDao.update(cart);

					} else {
						// Guest cart
						ObjectMapper mapper = new ObjectMapper();
						List<GuestCartItemDTO> guestItems = readGuestCartCookie(request, mapper);
						boolean found = false;
						for(GuestCartItemDTO item : guestItems) {
							if (item.getSku().equals(sku)) {
								item.setQuantity(item.getQuantity() + quantity);
								found = true;
								break;
							}
						}
						if (!found) {
							guestItems.add(new GuestCartItemDTO(sku, quantity));
						}

						saveGuestCartCookie(response, guestItems, mapper);
					}
				}

			}
				response.sendRedirect(request.getContextPath() + "/CartServlet?action=view");
			
		} catch(Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
	}
	
	// Utility Methods
	private CartBean createEmptyCart(String email) {
        CartBean cart = new CartBean();
        cart.setUserEmail(email);
        cart.setTotalPrice(0.0);
        cart.setVariants(new HashMap<>());
        return cart;
    }

    private void recalculateAndSetTotal(CartBean cart) {
        double total = 0.0;
        for (CartItemBean item : cart.getVariants().values()) {
            total += item.getVariant().getPrice() * item.getSelectedQuantity();
        }
        cart.setTotalPrice(total);
    }
    
    // Read cookies from request
    private List<GuestCartItemDTO> readGuestCartCookie(HttpServletRequest request, ObjectMapper mapper) throws IOException{
    	Cookie[] cookies = request.getCookies();
    	if(cookies != null) {
    		for(Cookie cookie : cookies) {
    			if("guest_cart".equals(cookie.getName()) && !cookie.getName().isEmpty()){
    				String decodedJson = URLDecoder.decode(cookie.getValue(), "UTF-8");
    				return mapper.readValue(decodedJson, new TypeReference<List<GuestCartItemDTO>>(){});
    			}
    		}
    	}
    	return new ArrayList<>();
    }
    
    private void saveGuestCartCookie(HttpServletResponse response, List<GuestCartItemDTO> guestCart, ObjectMapper mapper) throws IOException{
    	String cartJson = mapper.writeValueAsString(guestCart);
    	String encodedJson = URLEncoder.encode(cartJson, "UTF-8");
    	
    	Cookie cookie = new Cookie("cart_guest", encodedJson);
    	cookie.setPath("/");
    	cookie.setMaxAge(60 * 60 * 24 * 7); // Expires in 7 days
    	response.addCookie(cookie);
    }
    
    private CartBean getCartFromCookie(HttpServletRequest request) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<GuestCartItemDTO> guestItems = readGuestCartCookie(request, mapper);
		
		CartBean tempCart = createEmptyCart(null);
		Map<Long, CartItemBean> items = tempCart.getVariants();
		
		guestItems.forEach(item -> {
			VariantBean variant = productDao.findVariantBySku(item.getSku());
			if(variant != null) {
				CartItemBean newItem = new CartItemBean(variant, item.getQuantity());
				items.put(variant.getId(), newItem);
			}
		});
    	
    	recalculateAndSetTotal(tempCart);
    	return tempCart;
    }

}
