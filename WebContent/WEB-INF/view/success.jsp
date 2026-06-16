<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    
    String successMessage = (String) session.getAttribute("successMessage");    // Recupero il messaggio di successo dalla sessione
    
    if (successMessage != null){
    	session.removeAttribute("successMessage");
    } 
    else {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        
        return;
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ordine completato - Iron Axis</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/fragment/header.jsp" />
    <jsp:include page="/WEB-INF/fragment/menu.jsp" />

    <main class="success-main">
        <div class="success-card">
            <div class="success-icon">&#10004;</div>
            <h2>Ordine ricevuto!</h2>
            
            <div class="success-message">
                <%= successMessage %>
            </div>
            
            <p>Puoi controllare lo stato della spedizione in qualsiasi momento all'interno della tua area personale.</p>

            <a href="<%= request.getContextPath() %>/index.jsp" class="btn-home">Torna al negozio</a>
        </div>
    </main>

    <jsp:include page="/WEB-INF/fragment/footer.jsp" />
</body>
</html>