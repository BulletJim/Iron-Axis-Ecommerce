<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.backend.model.bean.CartBean" %>
<%@ page import="it.unisa.backend.model.bean.CartItemBean" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.DecimalFormat" %>
<%
    CartBean carrello = (CartBean) session.getAttribute("cart");
    String estimatedDelivery = (String) request.getAttribute("estimatedDelivery");
    Double totalPrice = (Double) request.getAttribute("totalPrice");
    DecimalFormat df = new DecimalFormat("0.00");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Riepilogo Ordine - Iron Axis</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
</head>
<body>
    <jsp:include page="/WEB-INF/fragment/header.jsp" />
    <jsp:include page="/WEB-INF/fragment/menu.jsp" />

    <main class="order-container">
        <div class="order-card">
            <h2>Riepilogo dell'ordine</h2>
            <p>Controlla gli articoli selezionati e la data di consegna prima di procedere al pagamento sicuro.</p>
            
            <div class="order-alert">
                <strong>Giorno stimato di consegna:</strong> <span><%= estimatedDelivery != null ? estimatedDelivery : "" %></span>
            </div>

            <table class="order-table">
                <thead>
                    <tr>
                        <th>Codice SKU</th>
                        <th>Taglia / Variante</th>
                        <th>Gusto</th>
                        <th>Prezzo unitario</th>
                        <th>Quantità</th>
                        <th>Subtotale</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (carrello != null && carrello.getVariants() != null) {
                        	
                            for (Map.Entry<Long, CartItemBean> entry : carrello.getVariants().entrySet()) {
                            	
                                CartItemBean item = entry.getValue();
                                double prezzoUnitario = item.getVariant().getPrice();
                                double subtotale = prezzoUnitario * item.getSelectedQuantity();
                    %>
                                <tr>
                                    <td data-label="Codice SKU"><%= item.getVariant().getSku() %></td>
                                    <td data-label="Taglia"><%= item.getVariant().getSize() %></td>
                                    <td data-label="Gusto"><%= item.getVariant().getFlavour() %></td>
                                    <td data-label="Prezzo Unitario">€ <%= df.format(prezzoUnitario) %></td>
                                    <td data-label="Quantità"><%= item.getSelectedQuantity() %></td>
                                    <td data-label="Subtotale">€ <%= df.format(subtotale) %></td>
                                </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>

            <div class="order-total">
                Totale complessivo (IVA inclusa): € <%= totalPrice != null ? df.format(totalPrice) : "0,00" %>
            </div>
            
            <div class="action-bar">
                <a href="<%= request.getContextPath() %>/PaymentServlet" class="btn-pay">Procedi al pagamento</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/fragment/footer.jsp" />
</body>
</html>