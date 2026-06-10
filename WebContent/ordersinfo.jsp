<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/info.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Iron Axis - Info Ordini</title>
</head>
<body>
	<%@ include file="/WEB-INF/fragment/header.jsp"%>
	
	<%@ include file="/WEB-INF/fragment/menu.jsp"%>
	
	<main class="info-container">
	
	<h1>Spedizioni e resi</h1>
	
	<h2>Tempi di consegna:</h2>
	<p>Gli ordini vengono elaborati dopo circa 24 ore dalla conferma del pagamento</p>
	
	<ul>
		<li><b>Spedizioni standard</b>: 3-5 giorni lavorativi</li>
		<li><b>Spedizioni express</b>: 1-2 giorni lavorativi</li>
	</ul>
	
	<h2>Costi di spedizione:</h2>
	
	<ul>
		<li>Ordini inferiori a 29,99€: 4,99€</li>
		<li>Ordini superiori a 29,99€: Spedizione gratuita</li>
	</ul>
	
	<h2>Politica di reso</h2>
	
	<p>È possibile richiedere il reso entro 14 giorni dalla consegna del prodotto.</p>
	<p>Soddisfare i seguenti requisiti per ottenere il reso:</p>

    <ul>
        <li>Prodotto integro e non utilizzato</li>
        <li>Confezione originale presente</li>
        <li>Prova d'acquisto obbligatoria</li>
    </ul>
	
	<h2>Rimborso</h2>

    <p>Dopo la verifica del prodotto restituito, il rimborso verrà effettuato entro 7-14 giorni lavorativi sul metodo di pagamento utilizzato.</p>
	
	</main>
	
	<%@ include file="/WEB-INF/fragment/footer.jsp"%>
</body>
</html>