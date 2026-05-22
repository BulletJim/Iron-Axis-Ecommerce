<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isErrorPage="true"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<link type="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<title>Error 403</title>
</head>

<body>
	<%-- <%@ include file="/fragments/header.jsp"%> --%>

	<%-- <%@ include file="/fragments/menu.jsp"%> --%>

	<main class="error-container">
		<div class="error-content">
			<h1>Error 403</h1>
			<p>Forbidden.</p>
			<a href="${pageContext.request.contextPath}/index.jsp"
				class="btn-primary">Torna alla Home</a>
		</div>
	</main>

	<%-- <%@ include file="/fragments/footer.jsp"%> --%>
</body>

</html>