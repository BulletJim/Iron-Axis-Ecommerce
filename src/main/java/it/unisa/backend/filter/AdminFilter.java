package it.unisa.backend.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.backend.model.bean.UserBean;

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter extends HttpFilter implements Filter {
       
	private static final long serialVersionUID = 1L;
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		HttpSession session = httpRequest.getSession(false);
		
		boolean isAdmin = false;
		
		if(session != null) {
			UserBean user = (UserBean)session.getAttribute("loggedUser");
			if(user != null && "admin".equalsIgnoreCase(user.getRole())) {
				isAdmin = true;
			}
		}
		
		if(isAdmin) {
			chain.doFilter(httpRequest, httpResponse);
		} else {
			//Not an admin
			if(session != null && session.getAttribute("loggedUser") != null) {
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: logged user is not an admin");
			} else {
				// not a logged user
				request.setAttribute("errorMessage", "Autenticazione richiesta!");
				request.getRequestDispatcher("/LoginServlet").forward(httpRequest, httpResponse);
			}
		}
		
	}
	
	@Override
    public void destroy() {

    }

}
