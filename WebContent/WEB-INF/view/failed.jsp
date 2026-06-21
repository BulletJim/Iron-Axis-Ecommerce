<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ordine non completato - Iron Axis</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/failed.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/fragment/header.jsp" />
    <jsp:include page="/WEB-INF/fragment/menu.jsp" />

    <main class="failed-main">
        <div class="failed-card">
            <div class="failed-icon">
                <i class="fa-solid fa-circle-xmark"></i>
            </div>
            <h2>Oops! Qualcosa è andato storto</h2>
            <p>Non è stato possibile elaborare il tuo ordine. Il problema potrebbe essere dovuto a un'interruzione della connessione o a un rifiuto del metodo di pagamento. I tuoi articoli sono ancora al sicuro nel carrello.</p>

            <div class="failed-actions">
                <a href="/CartServlet?action=view" class="btn-retry">Torna al carrello</a>
                <a href="<%= request.getContextPath() %>/index.jsp" class="btn-home">Torna alla home</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/fragment/footer.jsp" />
</body>
</html>