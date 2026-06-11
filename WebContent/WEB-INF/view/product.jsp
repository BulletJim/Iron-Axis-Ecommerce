<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="it.unisa.backend.model.bean.ProductBean" %>
<%@ page import="it.unisa.backend.model.bean.VariantBean" %>
<%@ page import="it.unisa.backend.model.bean.ReviewBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.TreeSet" %>

<%

	ProductBean product = (ProductBean)request.getAttribute("product");
	if(product == null){
		response.sendRedirect(request.getContextPath() + "index.jsp");
	}
	
	List<VariantBean> variants = product.getVariants();
	List<ReviewBean> reviews = product.getReviews();
	
	Set<String> flavours = new TreeSet<>();
	Set<String> sizes = new TreeSet<>();
	
	for(VariantBean v : variants){
		if(v.getFlavour() != null) flavours.add(v.getFlavour());
		if(v.getSize() != null) sizes.add(v.getSize());
	}
	
	double averageRating = 0.0;
    if (reviews != null && !reviews.isEmpty()) {
        double total = 0;
        for (ReviewBean r : reviews) total += r.getScore();
        averageRating = total / reviews.size();
    }
    int avgStars = (int) Math.round(averageRating);

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Product - Iron Axis</title>
</head>
<body>

<%@ include file="/WEB-INF/fragment/header.jsp"  %>

<%@ include file="/WEB-INF/fragment/menu.jsp"  %>

<main class="product-container">
	
	<article class="product-article">

			<div class="product-name-container">
				<h3><%=product.getName()%></h3>

				<div class="product-rating-overview">
					
					<span class="stars">
                    <% for(int i=1; i<=5; i++) {
                        if(i <= avgStars) { %>
                            <i class="fa-solid fa-star gold-star"></i>
                        <% } else { %>
                            <i class="fa-regular fa-star gray-star"></i>
                        <% }
                    } %>
                	</span>
					
					<span class="review-count">(<%=reviews != null ? reviews.size() : 0%>)
					</span> <span class="separator">|</span> <a class="write-review-link"
						href="${pageContext.request.contextPath}/ReviewServlet?id=<%= product.getId() %>">
						<i class="fa-solid fa-pen"></i> Scrivi una recensione
					</a>
				</div>
				<p class="product-category">Categoria: <span><%= product.getCategory() != null ? product.getCategory().getName() : "Generica" %></span></p>
			</div>
			
			<div class="image-info-container">
				<figure class="product-image-container">
					<img id="main-product-image" alt="<%= product.getName() %>" src="${pageContext.request.contextPath}/<%= !variants.isEmpty() ? variants.get(0).getImageUrl() : "image/default-image.png" %>">
				</figure>
			
				<div class="tab-buttons-container">
					<button type="button" id="btn-tab-desc" class="tab-btn active">Descrizione</button>
					<button type="button" id="btn-tab-rev" class="tab-btn">Recensioni</button>
				</div>

			</div>

			<section class="desc-rev-container">
				
				<div id="tab-content-desc" class="tab-content active">
					<h3>Informazioni sul prodotto</h3>
					<p><%=product.getDescription()%></p>
					<br>
					<p class="disclaimer-text">
						<em>* I valori nutrizionali e gli ingredienti possono variare
							leggermente in base al gusto selezionato. Non superare la dose
							giornaliera raccomandata.</em>
					</p>
				</div>

				<div id="tab-content-rev" class="tab-content">
					<h3>Recensioni del prodotto</h3>
					<div class="reviews-list">
						<%
						if (reviews != null && !reviews.isEmpty()) {
							for (ReviewBean review : reviews) {
								String email = review.getUserEmail();
								String maskedEmail = email;
								if (email != null && email.contains("@")) {
									String[] parts = email.split("@");
									if (parts[0].length() > 3)
										maskedEmail = parts[0].substring(0, 3) + "***@" + parts[1];
								}
						%>

						<div class="review-card">
							
							<div class="review-header">
								<span class="review-user"><i class="fa-solid fa-user"></i><%=maskedEmail%></span>
								<span class="review-date"><%= review.getReviewDate() %></span>
							</div>
							
							<div class="review-stars">
								<%
								for (int i = 1; i <= 5; i++) {
									if (i <= review.getScore()) {
								%><i class="fa-solid fa-star gold-star"></i>
								<%
								} else {
								%><i class="fa-regular fa-star gray-star"></i>
								<%
								}
								}
								%>
							</div>
							<h4><%=review.getTitle()%></h4>
							<p><%=review.getComment()%></p>
						</div>
						<%
						}
						} else {
						%>
						<p class="no-reviews">Non ci sono ancora recensioni per questo prodotto. Sii il primo a scriverne una!</p>
						<%
						}
						%>
					</div>
					
				</div>
				
			</section>

		</article>
	
	<aside class="cart-nutr-table-container">
	
		<div class="add-cart-container">
			<div class="price-box">
				<span class="currency">€<span id="product-price">0.00</span></span>
				<p id="stock-status" class="stock-info"></p>
			</div>
			
			<% if(!flavours.isEmpty()){ %>
			<label for="flavourMenu">Gusto:</label>
			<select class="flavour-menu" id="flavourMenu" name="flavourMenu">
				<% for(String flavour : flavours){ %> <option value="<%= flavour %>"><%= flavour %></option> <% } %>
			</select>
			<% } %>
			
			<% if(!sizes.isEmpty()) { %>
				<label for="sizeMenu">Formato:</label>
				<select class="size-menu" id="sizeMenu" name="sizeMenu">
					<% for(String s : sizes) { %> <option value="<%= s %>"><%= s %></option> <% } %>
				</select>
			<% } %>
			
			<form action="${pageContext.request.contextPath}/CartServlet" method="POST">
				<input type="hidden" id="selected-sku" name="productSku" value="">
				<input type="hidden" name="action" value="add">
				<button class="add-cart-btn" id="add-to-cart-btn" type="submit">Aggiungi al carrello</button>
			</form>
			
		</div>
		
		<div class="nutr-table-container">
			<h4>Tabella Nutrizionale</h4>
			<img id="nutritional-table-img" class="responsive-nutr-img" alt="Tabella Nutrizionale" src="">
		</div>
	
	</aside>

</main>

<%@ include file="/WEB-INF/fragment/footer.jsp"  %>

<div id="product-data" data-contextpath="${pageContext.request.contextPath}" data-variants='[
    <% for(int i=0; i<variants.size(); i++) { VariantBean variant = variants.get(i); %>
    {
        "sku": "<%= variant.getSku() %>",
        "size": "<%= variant.getSize() %>",
        "flavour": "<%= variant.getFlavour() %>",
        "price": <%= variant.getPrice() %>,
        "quantity": <%= variant.getQuantity() %>,
        "urlImage": "<%= variant.getImageUrl() %>",
        "nutrTableUrl": "<%= variant.getNutrTablUrl() %>"
    }<%= (i < variants.size() - 1) ? "," : "" %>
    <% } %>
]'></div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/product.js" defer></script>

</body>
</html>