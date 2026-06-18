<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.backend.model.bean.UserBean" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Il mio Profilo - Iron Axis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body>

    
   <%@ include file="/WEB-INF/fragment/header.jsp" %>

    <%
        UserBean user = (UserBean) request.getAttribute("user");
    %>

    <div class="profile-container">

        <h1>Il mio Profilo</h1>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="profile-alert error">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <% if (request.getAttribute("successMessage") != null) { %>
            <div class="profile-alert success">
                <%= request.getAttribute("successMessage") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/ProfileServlet" method="post">

            <div class="profile-form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" value="<%= user.getEmail() %>" readonly>
            </div>

            <div class="profile-form-group">
                <label for="firstName">Nome</label>
                <input type="text" id="firstName" name="firstName" value="<%= user.getFirstName() != null ? user.getFirstName() : "" %>" required>
            </div>

            <div class="profile-form-group">
                <label for="lastName">Cognome</label>
                <input type="text" id="lastName" name="lastName" value="<%= user.getLastName() != null ? user.getLastName() : "" %>" required>
            </div>

            <div class="profile-form-group">
                <label for="birthDate">Data di nascita</label>
                <input type="date" id="birthDate" name="birthDate" value="<%= user.getBirthDate() != null ? user.getBirthDate().toString() : "" %>">
            </div>

            <div class="profile-form-group">
                <label for="newPassword">Nuova Password</label>
                <input type="password" id="newPassword" name="newPassword" placeholder="Lascia vuoto per non modificarla">
            </div>

            <button type="submit" class="profile-submit-btn">Salva Modifiche</button>

        </form>
    </div>

   <%@ include file="/WEB-INF/fragment/footer.jsp" %>

</body>
</html>
