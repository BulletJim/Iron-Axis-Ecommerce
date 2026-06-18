<%@page import="java.util.Locale"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.backend.model.bean.CartBean" %>
<%@ page import="it.unisa.backend.model.bean.OrderBean" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    OrderBean order = (OrderBean)request.getSession().getAttribute("pendingOrder");
    double totalAmount = order != null ? order.getTotalAmount() : 0.0;
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Metodo di pagamento - Iron Axis</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/payment.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <jsp:include page="/WEB-INF/fragment/header.jsp" />
    <jsp:include page="/WEB-INF/fragment/menu.jsp" />

    <main class="payment-main">
        <div class="payment-wrapper">
            <div class="payment-header">
                <h2>Pagamento sicuro</h2>
                <p>Seleziona la modalità di pagamento preferita e inserisci le credenziali necessarie.</p>
            </div>
            
            <div class="total-banner">
                <span>Importo totale:</span>
                <span class="total-amount">€ <%= String.format(Locale.US, "%.2f", totalAmount) %></span>
            </div>

            <form id="paymentForm" method="POST" action="<%= request.getContextPath() %>/ConfirmOrderServlet">
                
                <div class="method-selector">
                    <label class="method-option active" id="labelCard">
                        <input type="radio" name="paymentMethod" value="Carta di Credito" checked>
                        <i class="fa-regular fa-credit-card"></i> Carta di credito
                    </label>
                    <label class="method-option" id="labelPaypal">
                        <input type="radio" name="paymentMethod" value="PayPal">
                        <i class="fa-brands fa-paypal"></i> PayPal
                    </label>
                </div>

                <div id="creditCardFields" class="form-section">
                    <div class="form-group">
                        <label for="cardHolder">Titolare della carta</label>
                        <div class="input-wrapper">
                            <i class="fa-regular fa-user"></i>
                            <input type="text" id="cardHolder" name="cardHolder" class="form-control" placeholder="Nome e Cognome">
                        </div>
                        <div class="error-msg" id="errHolder">Inserisci il nome e cognome completo.</div>
                    </div>

                    <div class="form-group">
                        <label for="cardNumber">Numero della carta</label>
                        <div class="input-wrapper">
                            <i class="fa-regular fa-credit-card"></i>
                            <input type="text" id="cardNumber" name="cardNumber" class="form-control" placeholder="0000 0000 0000 0000" maxlength="19">
                        </div>
                        <div class="error-msg" id="errCard">Inserisci un numero di carta valido.</div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="expDate">Scadenza</label>
                            <div class="input-wrapper">
                                <i class="fa-regular fa-calendar"></i>
                                <input type="text" id="expDate" name="expDate" class="form-control" placeholder="MM/AA" maxlength="5">
                            </div>
                            <div class="error-msg" id="errExp">Formato non valido.</div>
                        </div>
                        <div class="form-group">
                            <label for="cvv">CVV</label>
                            <div class="input-wrapper">
                                <i class="fa-solid fa-lock"></i>
                                <input type="text" id="cvv" name="cvv" class="form-control" placeholder="123" maxlength="3">
                            </div>
                            <div class="error-msg" id="errCvv">CVV non valido.</div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn-submit">
                    <i class="fa-solid fa-shield-halved"></i> Completa l'acquisto
                </button>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/fragment/footer.jsp" />

    <script src="<%= request.getContextPath() %>/js/payment.js"></script>
</body>
</html>