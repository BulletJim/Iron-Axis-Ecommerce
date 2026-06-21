<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/auth.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
	
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/18.2.1/css/intlTelInput.css">

	<script src="https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/18.2.1/js/intlTelInput.min.js"></script>
	
<title>Registrazione - IronAxis</title>
</head>
<body>

	<%@ include file="/WEB-INF/fragment/header.jsp"%>

	<%@ include file="/WEB-INF/fragment/menu.jsp"%>

	<main class="register-container">

		<h2>Crea il tuo account</h2>

		<form id="registerForm"
			action="${pageContext.request.contextPath}/RegisterServlet"
			method="POST" novalidate>

			<div class="form-group">
				<label for="firstName">Nome:</label> <input type="text"
					id="firstName" name="firstName" placeholder="Inserisci il tuo nome" maxlength="30" required>
			</div>

			<div class="form-group">
				<label for="LastName">Cognome:</label> <input type="text"
					id="lastName" name="lastName"
					placeholder="Inserisci il tuo cognome" maxlength="30" required>
			</div>

			<div class="form-group">
				<label for="email">Email:</label> <input type="email" id="email"
					name="email" placeholder="Inserisci la tua email"> 
					<span id="emailError" class="error-message"></span>
			</div>

			<div class="form-group">
    			<label for="dob">Data di Nascita:</label>
    			<input type="text" id="dob" name="dob" placeholder="GG/MM/AAAA" maxlength="10" required>
    			<span id="dobError" class="error-message"></span>
			</div>

			<div class="form-group">
				<label>Numero di Telefono:</label>
				<div id="phones-container">
					<div class="phone-row">
						<select name="phoneType" required>
							<option value="MOBILE">Cellulare</option>
							<option value="HOME">Casa</option>
							<option value="WORK">Lavoro</option>
						</select> 
						<input type="tel" name="phoneNumber" class="phone-input" placeholder="Inserisci il tuo numero" required>
					</div>
				</div>
				<span class="error-message phone-error"></span>
				<button type="button" id="add-phone-btn" class="btn-add-dynamic">+ Aggiungi un altro numero</button>
			</div>

			<div class="form-group">
				<label>Indirizzo di Spedizione:</label>
				<div id="addresses-container">
					<div class="address-group">
						<div class="input-row">
							<input type="text" name="street" placeholder="Via/Piazza" class="addr-street input-flex-1" maxlength="50" required>
							<input type="number" name="streetNumber" placeholder="N°" class="addr-civic input-civic" min="0" onkeydown="if(event.key === '-') event.preventDefault()" required>
						</div>
						<div class="input-row">
							<input type="text" name="city" placeholder="Città" class="addr-city input-flex-1" maxlength="50" required>
							<input type="text" name="prov" placeholder="Prov" class="addr-prov input-prov" required>
						</div>
						<div class="input-row">
							<input type="text" name="zipCode" placeholder="CAP" class="addr-zip-code input-zip-code" required> 
							<input type="text" name="country" placeholder="Nazione" class="addr-country input-flex-1" required>
						</div>
					</div>
				</div>
				<button type="button" id="add-address-btn" class="btn-add-dynamic">+ Aggiungi un altro indirizzo</button>
			</div>

			<div class="form-group">
				<label for="password">Password:</label> <input type="password"
					id="password" name="password" maxlength="20"
					placeholder="Inserisci la tua password"> 
					<span id="passwordError" class="error-message"></span>
			</div>

			<div class="form-group">
				<label for="confirmPassword">Conferma Password:</label> <input
					type="password" id="confirmPassword" name="confirmPassword"
					maxlength="20" placeholder="Conferma la tua password"> 
					<span id="confirmPasswordError" class="error-message"></span>
			</div>

			<button type="submit">Registrati</button>

		</form>

		<div class="link-container">
			<p>
				Hai già un account? <a
					href="${pageContext.request.contextPath}/LoginServlet">Accedi
					qui</a>
			</p>
		</div>

		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/register.js" defer></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/checkEmail.js" defer></script>

	</main>

	<%@ include file="/WEB-INF/fragment/footer.jsp"%>

</body>
</html>