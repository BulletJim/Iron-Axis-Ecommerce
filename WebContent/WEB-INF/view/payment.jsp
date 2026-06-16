<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.backend.model.bean.CartBean" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    CartBean carrello = (CartBean) session.getAttribute("cart");
    double totalPrice = 0.0;
    
    if (carrello != null) {
        totalPrice = carrello.getTotalPrice();
    }
    DecimalFormat df = new DecimalFormat("0.00");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Metodo di pagamento - Iron Axis</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/fragment/header.jsp" />
    <jsp:include page="/WEB-INF/fragment/menu.jsp" />

    <main class="payment-main">
        <div class="payment-card">
            <h2>Pagamento sicuro</h2>
            <p>Seleziona la modalità di pagamento preferita e inserisci le credenziali necessarie.</p>
            
            <div class="total-banner">
                Importo totale da pagare: € <%= df.format(totalPrice) %>
            </div>

            <form id="paymentForm" method="POST" action="<%= request.getContextPath() %>/ConfirmOrderServlet">
                
                <div class="method-selector">
                    <label class="method-option active" id="labelCard">
                        <input type="radio" name="paymentMethod" value="Carta di Credito" checked>
                        Carta di credito
                    </label>
                    <label class="method-option" id="labelPaypal">
                        <input type="radio" name="paymentMethod" value="PayPal">
                        PayPal
                    </label>
                </div>

                <div id="creditCardFields">
                    <div class="form-group">
                        <label for="cardHolder">Titolare della carta</label>
                        <input type="text" id="cardHolder" name="cardHolder" class="form-control" placeholder="Nome e Cognome" autofocus>
                        <div class="error-msg" id="errHolder">Inserisci il nome e cognome completo del titolare della carta.</div>
                    </div>

                    <div class="form-group">
                        <label for="cardNumber">Numero della carta di credito</label>
                        <input type="text" id="cardNumber" name="cardNumber" class="form-control" placeholder="Inserisci le 16 cifre della carta" maxlength="16">
                        <div class="error-msg" id="errCard">Inserisci un numero di carta valido composto da esattamente 16 cifre.</div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="expDate">Data scadenza (MM/AA)</label>
                            <input type="text" id="expDate" name="expDate" class="form-control" placeholder="MM/AA" maxlength="5">
                            <div class="error-msg" id="errExp">Formato non valido (MM/AA) o la carta inserita risulta scaduta</div>
                        </div>
                        <div class="form-group">
                            <label for="cvv">Codice CVV</label>
                            <input type="text" id="cvv" name="cvv" class="form-control" placeholder="3 cifre sul retro" maxlength="3">
                            <div class="error-msg" id="errCvv">Il codice CVV deve contenere esattamente 3 cifre numeriche</div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn-submit">Completa l'acquisto</button>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/fragment/footer.jsp" />

    <script src="<%= request.getContextPath() %>/js/payment.js"></script>
</body>
</html>