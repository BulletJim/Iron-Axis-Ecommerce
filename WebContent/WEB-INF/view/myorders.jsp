<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.backend.model.bean.OrderBean" %>
<%@ page import="it.unisa.backend.model.bean.OrderItemBean" %>
<%@ page import="it.unisa.backend.model.bean.UserBean" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I miei Ordini – Iron Axis</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/myorders.css">
</head>
<body>

<%@ include file="/WEB-INF/fragment/header.jsp" %>
<%@ include file="/WEB-INF/fragment/menu.jsp" %>

<%
    List<OrderBean> orders = (List<OrderBean>) request.getAttribute("orders");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<main class="orders-container">
    <h1><i class="fas fa-box-open"></i> I miei Ordini</h1>

    <% if (orders == null || orders.isEmpty()) { %>
        <div class="orders-empty">
            <i class="fas fa-shopping-bag"></i>
            <p>Non hai ancora effettuato nessun ordine.</p>
            <a href="${pageContext.request.contextPath}/CatalogServlet" class="orders-shop-btn">
                Vai al Catalogo
            </a>
        </div>

    <% } else { %>
        <% for (OrderBean order : orders) { %>
            <div class="order-card">

                <div class="order-card-header">
                    <div class="order-meta">
                        <span class="order-id">#<%= order.getId() %></span>
                        <% if (order.getCreatedAt() != null) { %>
                            <span class="order-date">
                                <i class="fas fa-calendar-alt"></i>
                                <%= order.getCreatedAt().format(fmt) %>
                            </span>
                        <% } %>
                    </div>
                    <div class="order-right">
                        <span class="order-status status-<%= order.getStatus().name().toLowerCase() %>">
                            <%= getStatusLabel(order.getStatus().name()) %>
                        </span>
                        <span class="order-total">
                            € <%= String.format("%.2f", order.getTotalAmount()) %>
                        </span>
                    </div>
                </div>

                <button class="order-toggle-btn" onclick="toggleDetails(this)">
                    <i class="fas fa-chevron-down"></i> Mostra prodotti
                </button>

                <div class="order-details" style="display: none;">
                    <% if (order.getItems() == null || order.getItems().isEmpty()) { %>
                        <p class="order-no-items">Nessun prodotto disponibile per questo ordine.</p>
                    <% } else { %>
                        <table class="order-items-table">
                            <thead>
                                <tr>
                                    <th>Prodotto (ID Variante)</th>
                                    <th>Quantità</th>
                                    <th>Prezzo unitario</th>
                                    <th>IVA</th>
                                    <th>Subtotale</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (OrderItemBean item : order.getItems()) { %>
                                    <tr>
                                        <td>Variante #<%= item.getVariant().getId() %></td>
                                        <td><%= item.getQuantity() %></td>
                                        <td>€ <%= String.format("%.2f", item.getPriceAtPurchase()) %></td>
                                        <td><%= String.format("%.0f", item.getVat()) %>%</td>
                                        <td>€ <%= String.format("%.2f", item.getPriceAtPurchase() * item.getQuantity()) %></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } %>
                </div>

            </div>
        <% } %>
    <% } %>
</main>

<%@ include file="/WEB-INF/fragment/footer.jsp" %>

<script>
    function toggleDetails(btn) {
        const card = btn.closest('.order-card');
        const details = card.querySelector('.order-details');
        const isVisible = details.style.display !== 'none';
        details.style.display = isVisible ? 'none' : 'block';
        btn.innerHTML = isVisible
            ? '<i class="fas fa-chevron-down"></i> Mostra prodotti'
            : '<i class="fas fa-chevron-up"></i> Nascondi prodotti';
    }
</script>

</body>
</html>

<%!
    private String getStatusLabel(String status) {
        if (status == null) return "Sconosciuto";
        switch (status.toUpperCase()) {
            case "PENDING":    return "In attesa";
            case "CONFIRMED":  return "Confermato";
            case "SHIPPED":    return "Spedito";
            case "DELIVERED":  return "Consegnato";
            case "CANCELLED":  return "Annullato";
            default:           return status;
        }
    }
%>
