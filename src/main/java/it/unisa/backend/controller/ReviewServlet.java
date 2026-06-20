package it.unisa.backend.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.backend.model.bean.ReviewBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.ReviewDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/ReviewServlet")
public class ReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private ReviewDAO reviewDao;

	@Override
	public void init() throws ServletException {
		reviewDao = new ReviewDAO(DBManager.getDataSource());
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
		if(loggedUser == null) {
			request.getSession().setAttribute("errorMessage", "Effettua il login per continuare");
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		
		String productIdString = request.getParameter("id");
		String productName = request.getParameter("name");
		if(productIdString == null || productName == null || productName.trim().isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product id  or product name not found");
			return;
		}
		
		try {
			Long productId = Long.parseLong(productIdString);
			boolean alreadyReviewed = reviewDao.findByUserAndProduct(loggedUser.getEmail(), productId) != null;
			if(alreadyReviewed) {
				request.getSession().setAttribute("errorMessage", "Hai già recensito questo prodotto");
				response.sendRedirect(request.getContextPath() + "/ProductServlet?id=" + productId);
				return;
			}
			request.setAttribute("productId", productId);
			request.setAttribute("productName", productName.trim());
			request.getRequestDispatcher("/WEB-INF/view/review.jsp").forward(request, response);
			
		} catch(NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Product Id");
		}catch(Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		}	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		UserBean loggedUser = (UserBean) request.getSession().getAttribute("loggedUser");
		if(loggedUser == null) {
			request.getSession().setAttribute("errorMessage", "Effettua il login per continuare");
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		
		String productIdString = request.getParameter("productId");
		String title = request.getParameter("title");
		String scoreString = request.getParameter("score");
		String comment = request.getParameter("comment");
		
		try {
			Long productId = Long.parseLong(productIdString);
			int score = Integer.parseInt(scoreString.trim());
			if(score < 1 || score > 5) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Score Number");
				return;
			}
			boolean alreadyReviewed = reviewDao.findByUserAndProduct(loggedUser.getEmail(), productId) != null;
			if(alreadyReviewed) {
				request.getSession().setAttribute("errorMessage", "Hai già recensito questo prodotto");
				response.sendRedirect(request.getContextPath() + "/ProductServlet?id=" + productId);
				return;
			}
			
			ReviewBean review = new ReviewBean();
			review.setProductId(productId);
			review.setUserEmail(loggedUser.getEmail());
			review.setTitle(title);
			review.setScore(score);
			review.setComment(comment);
			review.setReviewDate(LocalDateTime.now());
			
			if(reviewDao.save(review)) {
				request.getSession().setAttribute("successMessage", "Recensione registrata con successo");
				response.sendRedirect(request.getContextPath() + "/ProductServlet?id=" + productId);
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
			}
			
		} catch(NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Parameters");
		}catch(Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
		
	}

}
