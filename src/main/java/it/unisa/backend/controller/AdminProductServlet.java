package it.unisa.backend.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.unisa.backend.model.bean.CategoryBean;
import it.unisa.backend.model.bean.ProductBean;
import it.unisa.backend.model.bean.UserBean;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/AdminProductServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)

public class AdminProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "images" + File.separator + "products";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        UserBean user = (UserBean) request.getSession().getAttribute("loggedUser");
        
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            
            return;
        }

        String action = request.getParameter("action");
        ProductDAO productDao = new ProductDAO(DBManager.getDataSource());

        if ("edit".equals(action)) {
            try {
            	
                long id = Long.parseLong(request.getParameter("id"));
                ProductBean productToEdit = productDao.findById(id);
                request.setAttribute("productToEdit", productToEdit);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        List<ProductBean> products = productDao.findAll(); 
        request.setAttribute("products", products);

        request.getRequestDispatcher("/WEB-INF/view/dashboard.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        UserBean user = (UserBean) request.getSession().getAttribute("loggedUser");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }

        String action = request.getParameter("action");
        ProductDAO productDao = new ProductDAO(DBManager.getDataSource());

        try {
         
            if ("delete".equals(action)) {
                long id = Long.parseLong(request.getParameter("id"));
                if (productDao.delete(id)) {
                    request.getSession().setAttribute("successMessage", "Prodotto rimosso con successo!");
                } else {
                    request.getSession().setAttribute("errorMessage", "Errore nella rimozione.");
                }
            } 
           
            else if ("save".equals(action)) {
                String idStr = request.getParameter("id");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String categoryIdStr = request.getParameter("categoryId");

                if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty() || categoryIdStr == null) {
                    request.getSession().setAttribute("errorMessage", "Dati non validi: compila tutti i campi obbligatori.");
                    response.sendRedirect(request.getContextPath() + "/AdminProductServlet");
                    return;
                }

                ProductBean product = new ProductBean();
                product.setName(name.trim());
                product.setDescription(description.trim());
                
                CategoryBean cat = new CategoryBean();
                cat.setId(Long.parseLong(categoryIdStr));
                product.setCategory(cat);

                if (idStr == null || idStr.trim().isEmpty()) {
                    productDao.save(product);
                    request.getSession().setAttribute("successMessage", "Nuovo prodotto aggiunto al catalogo!");
                } 
                else {
                    product.setId(Long.parseLong(idStr));
                    productDao.update(product);
                    request.getSession().setAttribute("successMessage", "Prodotto aggiornato correttamente!");
                }
            }
            
            // OPERAZIONE 3: AGGIUNTA VARIANTE DETTAGLIATA (CON DOPPIO UPLOAD DI FILE)
            else if ("addVariant".equals(action)) {
                String productIdStr = request.getParameter("productId");
                String sku = request.getParameter("sku");
                String flavour = request.getParameter("flavour");
                String size = request.getParameter("size");
                String priceStr = request.getParameter("price");
                String vatStr = request.getParameter("vat");
                String quantityStr = request.getParameter("quantity");

                if (productIdStr == null || sku == null || priceStr == null || vatStr == null || quantityStr == null ||
                    sku.trim().isEmpty() || priceStr.trim().isEmpty() || vatStr.trim().isEmpty() || quantityStr.trim().isEmpty()) {
                    request.getSession().setAttribute("errorMessage", "Errore: Compila tutti i campi obbligatori della variante.");
                    response.sendRedirect(request.getContextPath() + "/AdminProductServlet");
                    return;
                }

                long productId = Long.parseLong(productIdStr);
                double price = Double.parseDouble(priceStr);
                double vat = Double.parseDouble(vatStr);
                int quantity = Integer.parseInt(quantityStr);

                String uploadPath = request.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String imageUrl = null;
                Part imgPart = request.getPart("productImage");
                if (imgPart != null && imgPart.getSize() > 0) {
                    String imgName = imgPart.getSubmittedFileName();
                    imgPart.write(uploadPath + File.separator + imgName);
                    imageUrl = "images/products/" + imgName; 
                }

                String nutrTablUrl = null;
                Part nutrPart = request.getPart("nutritionalTable");
                if (nutrPart != null && nutrPart.getSize() > 0) {
                    String nutrName = nutrPart.getSubmittedFileName();
                    nutrPart.write(uploadPath + File.separator + nutrName);
                    nutrTablUrl = "images/products/" + nutrName; 
                }

                String insertVariantSQL = "INSERT INTO variants (product_id, sku, size, vat, price, quantity, flavour, image_url, nutr_tabl_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (Connection connection = DBManager.getDataSource().getConnection();
                     PreparedStatement ps = connection.prepareStatement(insertVariantSQL)) {
                    
                    ps.setLong(1, productId);
                    ps.setString(2, sku.trim());
                    ps.setString(3, (size != null && !size.trim().isEmpty()) ? size.trim() : null);
                    ps.setDouble(4, vat);
                    ps.setDouble(5, price);
                    ps.setInt(6, quantity);
                    ps.setString(7, (flavour != null && !flavour.trim().isEmpty()) ? flavour.trim() : null);
                    ps.setString(8, imageUrl);
                    ps.setString(9, nutrTablUrl);

                    if (ps.executeUpdate() > 0) {
                        request.getSession().setAttribute("successMessage", "Variante tecnica associata con successo!");
                    } else {
                        request.getSession().setAttribute("errorMessage", "Errore nel salvataggio della variante nel database.");
                    }
                }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Si è verificato un errore critico di sistema.");
        }
        
        response.sendRedirect(request.getContextPath() + "/AdminProductServlet");
    }
}