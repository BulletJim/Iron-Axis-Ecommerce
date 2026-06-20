<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    Long productId = (Long) request.getAttribute("productId");
    String productName = (String) request.getAttribute("productName");

    if (productId == null || productName == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Scrivi una Recensione - Iron Axis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <%@ include file="/WEB-INF/fragment/header.jsp" %>
    <%@ include file="/WEB-INF/fragment/menu.jsp" %>

    <main class="review-container">
    	<h2>Recensisci: <%= productName %></h2>
    
    	<form action="${pageContext.request.contextPath}/ReviewServlet" method="POST">
        	<input type="hidden" name="productId" value="<%= productId %>">

        	<div class="form-group">
            	<label for="score">Valutazione (Stelle):</label>
            	<select name="score" id="score" required>
                	<option value="5" selected>5 - Eccellente</option>
                	<option value="4">4 - Molto Buono</option>
                	<option value="3">3 - Nella media</option>
                	<option value="2">2 - Scarso</option>
                	<option value="1">1 - Pessimo</option>
            	</select>
        	</div>

        	<div class="form-group">
            	<label for="title">Titolo della recensione:</label>
            	<input type="text" id="title" name="title" placeholder="Riassumi la tua esperienza" maxlength="50" required>
        	</div>

        	<div class="form-group">
            	<label for="comment">Il tuo commento (opzionale):</label>
            	<textarea id="comment" name="comment" placeholder="Cosa ti è piaciuto o non ti è piaciuto?"></textarea>
        	</div>

        	<button type="submit">Invia Recensione</button>
        
        	<div class="cancel-link-wrapper">
            	<a href="${pageContext.request.contextPath}/ProductServlet?id=<%= productId %>">Annulla e torna al prodotto</a>
        	</div>
    	</form>
	</main>

    <%@ include file="/WEB-INF/fragment/footer.jsp" %>

</body>
</html>