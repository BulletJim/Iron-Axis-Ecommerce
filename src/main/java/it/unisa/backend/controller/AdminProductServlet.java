package it.unisa.backend.controller;

import java.io.IOException;
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
import it.unisa.backend.model.bean.VariantBean;
import it.unisa.backend.model.dao.impl.ProductDAO;
import it.unisa.backend.model.db.DBManager;

@WebServlet("/admin/AdminProductServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)

public class AdminProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProductDAO productDao;
    
    @Override
	public void init() throws ServletException {
		productDao = new ProductDAO(DBManager.getDataSource());
	}

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

        request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        UserBean user = (UserBean) request.getSession().getAttribute("loggedUser");
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");
            return;
        }

        String action = request.getParameter("action");

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
                    response.sendRedirect(request.getContextPath() + "/admin/AdminProductServlet");
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
                    response.sendRedirect(request.getContextPath() + "/admin/AdminProductServlet");
                    return;
                }

                long productId = Long.parseLong(productIdStr);
                double price = Double.parseDouble(priceStr);
                double vat = Double.parseDouble(vatStr);
                int quantity = Integer.parseInt(quantityStr);

                VariantBean variant = new VariantBean();
                variant.setProductId(productId);
                variant.setSku(sku.trim());
                variant.setSize(size.trim());
                variant.setFlavour(flavour.trim());
                variant.setPrice(price);
                variant.setVat(vat);
                variant.setQuantity(quantity);

                Part imgPart = request.getPart("productImage");
                if (imgPart != null && imgPart.getSize() > 0) {
                    variant.setImageStream(imgPart.getInputStream());
                }

                Part nutrPart = request.getPart("nutritionalTable");
                if (nutrPart != null && nutrPart.getSize() > 0) {
                    variant.setNutrTablStream(nutrPart.getInputStream());
                }
                
                if (productDao.saveVariant(variant)) {
                    request.getSession().setAttribute("successMessage", "Variante aggiunta con successo");
                } else {
                    request.getSession().setAttribute("errorMessage", "Errore di inserimento variante");
                }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Si è verificato un errore critico di sistema.");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/AdminProductServlet");
    }
}