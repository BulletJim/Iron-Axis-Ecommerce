<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Login - IronAxis</title>
</head>
<body>

	<%@ include file="/WEB-INF/fragment/header.jsp" %>
	
	<%@ include file="/WEB-INF/fragment/menu.jsp" %>
	
	<main class="login-container">
	
		<h2>Accedi al tuo account</h2>
		
		<form id="loginForm" action="${pageContext.request.contextPath}/LoginServlet" method="POST" novalidate>
		
			<div class="form-group">
				<label for="email">Email:</label>
				<input type="email" id="email" name="email" placeholder="Inserisci la tua email">
				<span id="emailError" class="error-message"></span>
			</div>
			
			<div class="form-group">
				<label for="password">Password:</label>
				<input type="password" id="password" name="password" placeholder="Inserisci la tua password">
				<span id="passwordError" class="error-message"></span>
			</div>
			
			<button type="submit">Accedi</button>
			
			<% if (request.getAttribute("error") != null){ %>
			<p class="error-message"><%= request.getAttribute("error") %></p>
			<% } %>
		
		</form>
		
		<div class="link-container">
			<p>Non hai ancora un account? <a href="${pageContext.request.contextPath}/RegisterServlet">Registrati ora!</a></p>
		</div>
	
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/login.js" defer></script>
	
	</main>

	<%@ include file="/WEB-INF/fragment/footer.jsp" %>

</body>
</html>