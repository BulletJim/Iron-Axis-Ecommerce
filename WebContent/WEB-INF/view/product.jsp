<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
			<!-- Insert product name -->
			<!-- Review Stars -->
			<a class="review-link" href="/ReviewServlet">Scrivi una recensione</a>
		</div>
		
		<div class="image-info-container">
			<figure class="product-image-container">
				<!-- Insert product image -->
			</figure>
		
			<p class="add-info">
				<!-- Istruzioni: Mescolare 30 g di prodotto (1 misurino) con 150 ml di liquido (acqua, latte scremato, ecc.).
					Avvertenze: Il prodotto non va inteso come sostituto ad una dieta variata. Non superare la dose giornaliera raccomandata. 
					Tenere fuori dalla portata dei bambini al di sotto dei 3 anni. Conservare in un luogo fresco e asciutto.
					*I valori nutrizionali e gli ingredienti sono del gusto Bon Bon Nutchoc, di conseguenza potrebbero variare leggermente negli altri gusti. -->
			</p>
		</div>
		
	</article>
	
	<section class="desc-rev-container">
		<!-- Double button for description and review. All in one -->
	</section>
	
	<aside class="cart-nutr-table-container">
	
		<div class="add-cart-container">
			<!-- in stock, flavour menu, price and add to cart button -->
			<select class="flavour-menu" name="flavourMenu">
				
			</select>
			<select class="size-menu" name="sizeMenu">
				
			</select>
			<button class="add-cart-btn" type="submit">Aggiungi al carrello</button>
		</div>
		
		<div class="nutr-table-container">
			<table class="nutr-table">
			
			</table>
		</div>
	
	</aside>

</main>

<%@ include file="/WEB-INF/fragment/footer.jsp"  %>

</body>
</html>