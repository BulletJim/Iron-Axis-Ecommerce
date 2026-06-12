package it.unisa.backend.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/SearchBarServlet")

public class SearchBarServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("search");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        if(query == null || query.trim().isEmpty()) {   //Se non c'è testo inserito restituisce un array vuoto
        	out.print("[]");
        	
        	return;
        }
        
        ProductDAO productDAO = new ProductDAO(DBManager.getDataSource());
        
        List<ProductBean> products = productDAO.searchProducts(query);  //Cerca i prodotti
        
        
        List<Map<String, Object>> suggestions = new ArrayList<>();   //Prende id e nome per il JSON
        
        for (ProductBean p : products) {
        	Map<String, Object> item = new HashMap<>();
        	item.put("id", p.getId());
        	item.put("name", p.getName());
        	suggestions.add(item);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, suggestions);
        out.flush();	
	}
}	