<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/catalog.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Iron Axis</title>
</head>

<body>
	<%@ include file="/WEB-INF/fragment/header.jsp"%>
	
	<%@ include file="/WEB-INF/fragment/menu.jsp"%>
	
	<main class="catalog-container">
        <header class="catalog-hero">
            <h1>Alimenta la tua Forza</h1>
            <p>Scopri la nostra selezione di integratori premium per il tuo allenamento.</p>
        </header>

        <section class="products-grid">
            
            <article class="product-card">
                <div class="product-image">
                    <img src="https://images.unsplash.com/photo-1579758629938-03607ccdbaba?q=80&w=400" alt="Proteine del siero">
                </div>
                <div class="product-info">
                    <span class="product-category">Proteine</span>
                    <h3 class="product-title">Whey Protein 100% Pure</h3>
                    <p class="product-price">€ 34,99</p>
                    <a href="#" class="btn-add-cart">
                        <i class="fas fa-shopping-cart"></i> Aggiungi
                    </a>
                </div>
            </article>

            <article class="product-card">
                <div class="product-image">
                    <img src="https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=400" alt="Creatina">
                </div>
                <div class="product-info">
                    <span class="product-category">Energia</span>
                    <h3 class="product-title">Creatina Monoidrato Micronizzata</h3>
                    <p class="product-price">€ 19,99</p>
                    <a href="#" class="btn-add-cart">
                        <i class="fas fa-shopping-cart"></i> Aggiungi
                    </a>
                </div>
            </article>

            <article class="product-card">
                <div class="product-image">
                    <img src="https://images.unsplash.com/photo-1611926653458-09294b3142bf?q=80&w=400" alt="Shaker">
                </div>
                <div class="product-info">
                    <span class="product-category">Accessori</span>
                    <h3 class="product-title">Shaker Acciaio Iron Axis 700ml</h3>
                    <p class="product-price">€ 14,50</p>
                    <a href="#" class="btn-add-cart">
                        <i class="fas fa-shopping-cart"></i> Aggiungi
                    </a>
                </div>
            </article>

        </section>
    </main>
	
	<%@ include file="/WEB-INF/fragment/footer.jsp"%>
</body>
</html>