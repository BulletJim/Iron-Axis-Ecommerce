<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.backend.model.bean.ProductBean, it.unisa.backend.model.bean.VariantBean, java.util.List" %>
<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
    if (products != null && !products.isEmpty()) {
        for (ProductBean p : products) {
            
            VariantBean variant = new VariantBean();
            if (p.getVariants() != null && !p.getVariants().isEmpty()) {
                variant = p.getVariants().get(0);
            }
%>
    <div class="product-card">
        
        <div class="product-image">
            <img src="${pageContext.request.contextPath}/<%= variant != null ? variant.getImageUrl() : "image/default-image.png" %>" alt="<%= p.getName() %>">
        </div>
        
        <div class="product-info">
            
            <div class="product-details-block">
                <span class="product-category">Risultato della ricerca</span>
                <a class="product-title" href="${pageContext.request.contextPath}/ProductServlet?id=<%= p.getId() %>"><%= p.getName() %></a>
            </div>
            
            <div class="product-action-block">
                <p class="product-price"><%= String.format(java.util.Locale.US, "%.2f", variant.getPrice()) %> &euro;</p>
                <p class="product-size"><%= variant.getSize() %></p> 
                <form class="product-cart-form" action="${pageContext.request.contextPath}/CartServlet" method="POST">
                    <input type="hidden" name="productSku" value="<%= variant.getSku() %>" required>
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="quantity" value="1">
                    <button class="btn-add-cart" type="submit" <%= variant == null ? "disabled" : "" %>>
                        <i class="fas fa-shopping-cart"></i> Aggiungi al Carrello
                    </button>
                </form>
            </div>
            
        </div>
    </div>
<% 
        }
    } else { 
%>
    <p class="no-data-msg">Nessun prodotto trovato con i filtri selezionati.</p>
<% } %>