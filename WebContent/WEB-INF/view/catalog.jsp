<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.backend.model.bean.ProductBean, it.unisa.backend.model.bean.VariantBean, it.unisa.backend.model.bean.CategoryBean, java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/catalog.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <title>Iron Axis - Catalogo</title>
</head>
<body>

	<%@ include file="/WEB-INF/fragment/header.jsp"%>
	<%@ include file="/WEB-INF/fragment/menu.jsp"%>
	
	<%
	    // Recupero delle liste per la Landing Page
	    List<ProductBean> topRated = (List<ProductBean>) request.getAttribute("topRated");
	    List<ProductBean> suggested = (List<ProductBean>) request.getAttribute("suggested");
	    List<ProductBean> recents = (List<ProductBean>) request.getAttribute("recents"); 
	    Long selectedCategoryId = (Long) request.getAttribute("selectedCategoryId");
	    boolean hasInitialFilter = (selectedCategoryId != null);

	    // Recupera la lista globale per la select dei filtri
	    @SuppressWarnings("unchecked")
	    List<CategoryBean> filterCategories = (List<CategoryBean>) application.getAttribute("globalCategories");
	%>
	
	<main class="catalog-container">
        <header class="catalog-hero">
            <h1>Alimenta la tua Forza</h1>
            <p>Scopri la nostra selezione di integratori premium per il tuo allenamento.</p>
        </header>

        <div class="catalog-layout">
            
            <aside class="filter-sidebar">
                <div class="sidebar-sticky-content">
                    <h3><i class="fas fa-sliders-h"></i> Filtra Prodotti</h3>
                    
                    <div class="filter-group">
                        <label for="filter-category">Categoria</label>
                        <select id="filter-category" name="categoryId">
                            <option value="" <%= (selectedCategoryId == null) ? "selected" : "" %>>Tutti i prodotti</option>
                            
                            <% 
                                if (filterCategories != null) {
                                    for (CategoryBean category : filterCategories) {
                                        boolean isSelected = (selectedCategoryId != null && selectedCategoryId.equals(category.getId()));
                            %>
                                <option value="<%= category.getId() %>" <%= isSelected ? "selected" : "" %>>
                                    <%= category.getName() %>
                                </option>
                            <% 
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label for="filter-max-price">Prezzo Massimo (€)</label>
                        <div class="price-input-wrapper">
                            <input type="number" id="filter-max-price" name="maxPrice" min="0" placeholder="Es. 50">
                        </div>
                    </div>

                    <div class="filter-group">
                        <label for="filter-sort">Ordina Per</label>
                        <select id="filter-sort" name="sortBy">
                            <option value="default">In Evidenza</option>
                            <option value="price_asc">Prezzo: dal più basso</option>
                            <option value="price_desc">Prezzo: dal più alto</option>
                            <option value="rating">Recensioni migliori</option>
                        </select>
                    </div>

                    <div class="filter-group checkbox-group">
                        <label class="pure-material-checkbox">
                            <input type="checkbox" id="filter-available" name="onlyAvailable" value="true">
                            <span>Solo prodotti disponibili</span>
                        </label>
                    </div>
                    
                    <button id="btn-clear-filters" class="btn-secondary">Azzera Filtri</button>
                </div>
            </aside>

            <div class="catalog-main-content">
                
                <div id="landing-sections-wrapper" class="<%= hasInitialFilter ? "initially-hidden" : "" %>">
                    
                    <section class="horizontal-area">
                        <div class="area-header">
                            <h2><i class="fas fa-star icon-star"></i> I Più Votati</h2>
                            <p>I preferiti dalla nostra community di atleti</p>
                        </div>
                        <div class="horizontal-scroll-container">
							<%
							if (topRated != null && !topRated.isEmpty()) {
								for (ProductBean p : topRated) {
									VariantBean variant = new VariantBean();
									if (p.getVariants() != null && !p.getVariants().isEmpty()) {
										variant = p.getVariants().get(0);
									}
							%>
							<div class="product-card">
								<div class="product-image">
									<img src="${pageContext.request.contextPath}/images/products/placeholder.jpg" alt="<%= p.getName() %>">
								</div>
								<span class="product-category">Top Rated</span> 
                                <a class="product-title" href="${pageContext.request.contextPath}/ProductServlet?id=<%= p.getId() %>"><%=p.getName()%></a>
								<p class="product-price"><%= String.format(java.util.Locale.US, "%.2f", variant.getPrice()) %> &euro;</p>
								<p class="product-size"><%= variant.getSize() %></p>

								<form class="product-cart-form" action="${pageContext.request.contextPath}/CartServlet" method="POST">
									<input type="hidden" name="productSku" value="<%= variant.getSku() %>" required> 
									<input type="hidden" name="action" value="add">
								    <input type="hidden" name="quantity" value="1">
									<button class="btn-add-cart" type="submit" <%= variant == null ? "disabled" : ""%>>
										<i class="fas fa-shopping-cart"></i> Aggiungi
									</button>
								</form>
							</div>
							<%
								}
							} else {
							%>
							<p class="no-data-msg">Nessun prodotto in evidenza al momento.</p>
							<% } %>
						</div>
                    </section>

                    <section class="horizontal-area">
                        <div class="area-header">
                            <h2><i class="fas fa-fire icon-fire"></i> Suggeriti per Te</h2>
                            <p>Scelti per i tuoi obiettivi di performance</p>
                        </div>
                        <div class="horizontal-scroll-container">
                            <% 
                                if (suggested != null && !suggested.isEmpty()) {
                                    for (ProductBean p : suggested) {
                                    	VariantBean variant = new VariantBean();
                                        if (p.getVariants() != null && !p.getVariants().isEmpty()) {
    										variant = p.getVariants().get(0);
                                        }
                            %>
                                <div class="product-card">
                                    <div class="product-image">
                                       <img src="${pageContext.request.contextPath}/images/products/placeholder.jpg" alt="<%= p.getName() %>">
                                    </div>
                                    <span class="product-category">Most Suggested</span>
                                    <a class="product-title" href="${pageContext.request.contextPath}/ProductServlet?id=<%= p.getId() %>"><%= p.getName() %></a>
                                    <p class="product-price"><%= String.format(java.util.Locale.US, "%.2f", variant.getPrice()) %> &euro;</p>
                                    <p class="product-size"><%= variant.getSize() %></p>
                                    
                                    <form class="product-cart-form" action="${pageContext.request.contextPath}/CartServlet" method="POST">
                                        <input type="hidden" name="productSku" value="<%= variant.getSku() %>" required>
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="quantity" value="1">
                                        <button class="btn-add-cart" type="submit">
                                            <i class="fas fa-shopping-cart"></i> Aggiungi
                                        </button>
                                    </form>
                                </div>
                            <% 
                                    }
                                } else { 
                            %>
                                <p class="no-data-msg">Nessun suggerimento disponibile.</p>
                            <% } %>
                        </div>
                    </section>

                    <% if (request.getSession().getAttribute("user") != null) { %>
                    <section class="horizontal-area">
                        <div class="area-header">
                            <h2><i class="fas fa-history"></i> Visitati di recente</h2>
                            <p>I prodotti che hai guardato ultimamente</p>
                        </div>
                        <div class="horizontal-scroll-container">
                            <% 
                                if (recents != null && !recents.isEmpty()) {
                                    for (ProductBean p : recents) {
                                    	VariantBean variant = new VariantBean();
                                        if (p.getVariants() != null && !p.getVariants().isEmpty()) {
    										variant = p.getVariants().get(0);
                                        }
                            %>
                                <div class="product-card">
                                    <div class="product-image">
                                       <img src="${pageContext.request.contextPath}/images/products/placeholder.jpg" alt="<%= p.getName() %>">
                                    </div>
                                    <span class="product-category">Visto di recente</span>
                                    <a class="product-title" href="${pageContext.request.contextPath}/ProductServlet?id=<%= p.getId() %>"><%= p.getName() %></a>
                                    <p class="product-price"><%= String.format(java.util.Locale.US, "%.2f", variant.getPrice()) %> &euro;</p>
                                    <p class="product-size"><%= variant.getSize() %></p>
                                    
                                    <form class="product-cart-form" action="${pageContext.request.contextPath}/CartServlet" method="POST">
                                        <input type="hidden" name="productSku" value="<%= variant.getSku() %>" required>
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="quantity" value="1">
                                        <button class="btn-add-cart" type="submit">
                                            <i class="fas fa-shopping-cart"></i> Aggiungi
                                        </button>
                                    </form>
                                </div>
                            <% 
                                    }
                                } else { 
                            %>
                                <p class="no-data-msg">Nessun prodotto visto di recente.</p>
                            <% } %>
                        </div>
                    </section>
                    <% } %>
                </div>

                <div id="search-results-wrapper" class="<%= hasInitialFilter ? "" : "initially-hidden" %>">
                    <div class="area-header">
                        <h2>Risultati della Ricerca</h2>
                        <p id="results-counter">Prodotti caricati in tempo reale</p>
                    </div>
                    <section id="products-ajax-container" class="products-grid">
                        <%@ include file="/WEB-INF/fragment/productGridSnippet.jsp"%>
                    </section>
                </div>

            </div>
        </div>
    </main>
	
	<%@ include file="/WEB-INF/fragment/footer.jsp"%>

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/catalog.js" defer></script>
</body>
</html>