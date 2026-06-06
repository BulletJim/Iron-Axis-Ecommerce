<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Registrazione - IronAxis</title>
</head>
<body>

	<%@ include file="/WEB-INF/fragment/header.jsp" %>
	
	<%@ include file="/WEB-INF/fragment/menu.jsp" %>
	
	<main class="register-container">
	
		<h2>Crea il tuo account</h2>
	
		<form id="registerForm" action="${pageContext.request.contextPath}/RegisterServlet" method="POST" novalidate>
		
			<div class="form-group">
				<label for="firstName">Nome:</label>
				<input type="text" id="firstName" name="firstName"  placeholder="Inserisci il tuo nome">
			</div>
			
			<div class="form-group">
				<label for="LastName">Cognome:</label>
				<input type="text" id="lastName" name="lastName"  placeholder="Inserisci il tuo cognome">
			</div>
			
			<div class="form-group">
				<label for="dateOfBirth">Data di Nascita:</label>
				<input type="date" id="dateOfBirth" name="dateOfBirth"  placeholder="Inserisci la tua data di nascita">
			</div>
			
			<div class="form-group">
				<label for="email">Email:</label>
				<input type="email" id="email" name="email"  placeholder="Inserisci la tua email">
				<span id="emailError" class="error-message"></span>
			</div>
			
			<div class="form-group">
				<label for="LastName">Password:</label>
				<input type="password" id="password" name="password"  maxlength="20" placeholder="Inserisci la tua password">
				<span id="passwordError" class="error-message"></span>
			</div>
			
			<div class="form-group">
				<label for="confirmPassword">Conferma Password:</label>
				<input type="password" id="confirmPassword" name="confirmPassword" maxlength="20"  placeholder="Conferma la tua password">
				<span id="confirmPasswordError" class="error-message"></span>
			</div>
			
			<button type="submit">Registrati</button>
			
			<% if (request.getAttribute("error") != null){ %>
			<p class="error-message"><%= request.getAttribute("error") %></p>
			<% } %>
		
		</form>
		
		<div class="link-container">
			<p>Hai già un account? <a href="${pageContext.request.contextPath}/LoginServlet">Accedi qui</a></p>
		</div>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/register.js" defer></script>
	
	</main>

	<%@ include file="/WEB-INF/fragment/footer.jsp" %>

</body>
</html>