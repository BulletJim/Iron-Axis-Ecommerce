<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.backend.model.bean.CartBean" %>
<%@ page import="it.unisa.backend.model.bean.CartItemBean" %>
<%@ page import="it.unisa.backend.model.bean.VariantBean" %>
<%
    CartBean cart = (CartBean) request.getAttribute("cart");
    boolean isCartEmpty = (cart == null || cart.getVariants() == null || cart.getVariants().isEmpty());
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Il tuo Carrello - IronAxis</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <%@ include file="/WEB-INF/fragment/header.jsp" %>
    
    <%@ include file="/WEB-INF/fragment/menu.jsp" %>
    
    <main class="cart-main">
        <div class="cart-container">
            <h1>Il tuo Carrello</h1>

            <% if (isCartEmpty) { %>
                <div class="empty-cart-msg">
                    <p>Il tuo carrello è attualmente vuoto. Abbiamo un sacco di ottimi prodotti che ti aspettano!</p>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="btn">Torna allo Shop</a>
                </div>
            <% } else { %>
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo Unitario</th>
                            <th>Quantità</th>
                            <th>Totale</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            for (CartItemBean item : cart.getVariants().values()) { 
                                VariantBean variant = item.getVariant();
                                double itemTotal = variant.getPrice() * item.getSelectedQuantity();
                        %>
                            <tr>
                                <td data-label="Prodotto">
                                    <div class="product-info">
                                        <img src="${pageContext.request.contextPath}/<%= variant.getImageUrl() %>" alt="Immagine Prodotto" class="product-img">
                                        <div class="product-details">
                                            <a class="product-title" href="ProductServlet?id=<%= variant.getProductId() %>"><%= variant.getSku() %></a>
                                            <p>Gusto: <%= variant.getFlavour() != null ? variant.getFlavour() : "N/D" %></p>
                                            <p>Formato: <%= variant.getSize() != null ? variant.getSize() : "N/D" %></p>
                                        </div>
                                    </div>
                                </td>
                                
                                <td data-label="Prezzo Unitario">
                                    <%= String.format("%.2f", variant.getPrice()) %> &euro;
                                </td>
                                
                                <td data-label="Quantità">
    								<div class="quantity-controls">
        								<button type="button" class="qty-btn minus-btn"><i class="fa-solid fa-minus"></i></button>
        								<input type="number" 
               										value="<%= item.getSelectedQuantity() %>" 
              										min="1" 
               										max="<%= variant.getQuantity() %>" 
               										class="quantity-input ajax-quantity" 
               										data-sku="<%= variant.getSku() %>" 
               										data-price="<%= variant.getPrice() %>"
               										readonly>
        								<button type="button" class="qty-btn plus-btn"><i class="fa-solid fa-plus"></i></button>
    								</div>
								</td>

								<td data-label="Totale"><strong> 
								<span class="item-subtotal" id="subtotal-<%=variant.getSku()%>">
									<%=String.format(java.util.Locale.US, "%.2f", itemTotal)%>
									</span> &euro;</strong>
								</td>

						<td data-label="Azioni">
                                    <form action="${pageContext.request.contextPath}/CartServlet" method="POST">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productSku" value="<%= variant.getSku() %>">
                                        <button type="submit" class="remove-btn"><i class="fa-solid fa-trash"></i> Rimuovi</button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>

                <div class="cart-summary">
					<div class="total-price">
						Totale Carrello: <span id="cart-grand-total"><%=String.format(java.util.Locale.US, "%.2f", cart.getTotalPrice())%></span>&euro;
					</div>
				<div>
                        <a href="${pageContext.request.contextPath}/CheckoutServlet" class="btn">Procedi al Checkout</a>
                    </div>
                </div>
            <% } %>
        </div>
    </main>

    <%@ include file="/WEB-INF/fragment/footer.jsp" %>

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/cart.js" defer></script>
</body>
</html>