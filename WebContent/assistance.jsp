<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/info.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Iron Axis - Assistenza Clienti</title>
</head>
<body>
	<%@ include file="/WEB-INF/fragment/header.jsp"%>
	
	<%@ include file="/WEB-INF/fragment/menu.jsp"%>
	
	<main class="info-container">
	<h1>Servizio clienti</h1>
	<h2>Benvenuto nel nostro servizio clienti</h2>
	<p>Siamo a tua disposizione per aiutarti in ogni fase dell'acquisto, dalla scelta dei prodotti all'assistenza post-vendita.</p>
	
	<h3>Come contattarci:</h3>
	<ul>
		<li> Telefono: <a href="tel: +390123456789"> +39 012 345 6789 </a> </li>
		<li> Email: <a href="mailto: info@ironaxis.it"> info@ironaxis.it</a></li>
	</ul>
	
	<h3>Orari assistenza:</h3>
	<ul>
		<li>Dal lunedì al venerdì: 9:00 - 17:00</li>
		<li>Sabato: 9:00 - 15:00</li>
		<li>Domenica: Chiuso</li>
	</ul>
	
	
	<h2>Domande frequenti</h2>
	
	<div class="faq-box">
	<h3>Come posso controllare lo stato del mio ordine?</h3>
	<p>Accedi alla tua area personale e consulta la sezione "I miei ordini."</p>
	</div>
	
	<div class="faq-box">
	<h3>Posso modificare o annullare il mio ordine?</h3>
	<p>Si è possibile modificare o annullare l'ordine finchè non viene affidato al corriere.</p>
	</div>
	
	<div class="faq-box">
	<h3>Cosa fare se ricevo un prodotto danneggiato?</h3>
	<p>Contatta il nostro servizio clienti entro 48 ore dalla consegna allegando fotografie del prodotto.</p>
	</div>
	
	<div class="faq-box">
	<h3>Supporto tecnico</h3>
	<p>Per problemi relativi al sito web, ai pagamenti o all'accesso all'account, contattaci tramite email/telefono.</p>
	</div>	
	
	</main>
	
	<%@ include file="/WEB-INF/fragment/footer.jsp"%>
</body>
</html>