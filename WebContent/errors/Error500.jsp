<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
<title>Internal Server Error - IronAxis</title>
</head>

<body>
	<%@ include file="/fragments/header.jsp"%>

	<%@ include file="/fragments/menu.jsp"%>

	<main class="error-container">
		<div class="error-content">
			<h1>Error 500</h1>
			<p>Internal Server Error.</p>
			<a href="${pageContext.request.contextPath}/index.jsp"
				class="btn-primary">Torna alla Home</a>
		</div>
	</main>

	<%@ include file="/fragments/footer.jsp"%>
</body>

</html>