package it.unisa.backend.controller;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/DisplayFileServlet")
public class DisplayFileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDao;

    @Override
    public void init() throws ServletException {
        this.productDao = new ProductDAO(DBManager.getDataSource());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sku = request.getParameter("sku");
        String type = request.getParameter("type");
        
        if (sku == null || sku.trim().isEmpty() || type == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti o non validi");
            return;
        }

        byte[] fileData = null;

        if ("image".equals(type)) {
            fileData = productDao.findVariantImageBySku(sku);
        } else if ("nutr".equals(type)) {
            fileData = productDao.findVariantNutrBySku(sku);
        }

        if (fileData != null && fileData.length > 0) {
            response.setContentType("image/jpeg"); 
            response.setContentLength(fileData.length);
            
            try (OutputStream out = response.getOutputStream()) {
                out.write(fileData);
                out.flush();
            }
        } else {

            response.sendRedirect(request.getContextPath() + "/images/placeholder.jpg");
        }
    }
}