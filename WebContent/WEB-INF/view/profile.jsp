<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.unisa.backend.model.bean.UserBean" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Il mio profilo - Iron Axis</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body class="prof-body">

    <%@ include file="/WEB-INF/fragment/header.jsp" %>
    <%@ include file="/WEB-INF/fragment/menu.jsp" %>

    <%;
        
        String formattedDob = "";
        
        if(loggedUser != null && loggedUser.getBirthDate() != null) {
            String dbDate = loggedUser.getBirthDate().toString(); 
            
            if(dbDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                String[] parts = dbDate.split("-");
                formattedDob = parts[2] + "/" + parts[1] + "/" + parts[0];
            }
            else {
                formattedDob = dbDate;
            }
        }
    %>

    <main class="prof-wrapper">
        
        <div class="prof-header">
            <h1>Il mio profilo</h1>
            <p>Gestisci i tuoi dati anagrafici e le tue credenziali di accesso.</p>
        </div>

        <div class="prof-card">
            <h2><i class="fas fa-user-circle"></i> Dati personali</h2>

            <form action="${pageContext.request.contextPath}/ProfileServlet" method="post" id="profileForm">

                <div class="prof-form-group">
                    <label for="email">Indirizzo Email</label>
                    <input type="email" id="email" name="email" value="<%= loggedUser != null ? loggedUser.getEmail() : "" %>" class="prof-input-readonly" readonly>
                </div>

                <div class="prof-form-group">
                    <label for="firstName">Nome</label>
                    <input type="text" id="firstName" name="firstName" value="<%= loggedUser != null && loggedUser.getFirstName() != null ? loggedUser.getFirstName() : "" %>" required>
                </div>

                <div class="prof-form-group">
                    <label for="lastName">Cognome</label>
                    <input type="text" id="lastName" name="lastName" value="<%= loggedUser != null && loggedUser.getLastName() != null ? loggedUser.getLastName() : "" %>" required>
                </div>

                <div class="prof-form-group">
                    <label for="birthDateDisplay">Data di nascita</label>
                    
                    <input type="text" id="birthDateDisplay" value="<%= formattedDob %>" placeholder="GG/MM/AAAA" required>
                    <input type="hidden" id="birthDate" name="birthDate" value="<%= loggedUser != null && loggedUser.getBirthDate() != null ? loggedUser.getBirthDate().toString() : "" %>">
                    
                    <span id="dobError" class="prof-error-text"></span>
                </div>

                <div class="prof-form-group">
                    <label for="newPassword">Nuova password</label>
                    <input type="password" id="newPassword" name="newPassword" placeholder="Lascia vuoto per non modificarla">
                </div>

                <div class="prof-form-actions">
                    <button type="submit" class="prof-btn-submit"><i class="fas fa-save"></i> Salva modifiche</button>
                </div>

            </form>
        </div>
    </main>

    <%@ include file="/WEB-INF/fragment/footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/profile.js" defer></script>

</body>
</html>
