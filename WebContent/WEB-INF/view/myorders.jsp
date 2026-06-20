<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.backend.model.bean.OrderBean" %>
<%@ page import="it.unisa.backend.model.bean.OrderItemBean" %>
<%@ page import="it.unisa.backend.model.bean.ProductBean" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I miei ordini – Iron Axis</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/myorders.css">
</head>
<body class="mo-body">

<%@ include file="/WEB-INF/fragment/header.jsp" %>
<%@ include file="/WEB-INF/fragment/menu.jsp" %>

<%
    List<OrderBean> orders = (List<OrderBean>) request.getAttribute("orders");
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products"); 
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<main class="mo-wrapper">
    <div class="mo-header">
        <h1>Area Cliente</h1>
        <p>Monitora lo storico dei tuoi ordini e scarica le relative fatture in formato PDF.</p>
    </div>

    <div class="mo-card">
        <h2><i class="fas fa-box-open"></i> Storico Ordini</h2>

        <div class="mo-table-container">
            <table class="mo-table">
                <thead>
                    <tr>
                        <th>ID ordine</th>
                        <th>Data transazione</th>
                        <th>Stato</th>
                        <th style="text-align: right;">Totale transato</th>
                        <th class="mo-action-cell">Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (orders == null || orders.isEmpty()) { %>
                        <tr>
                            <td colspan="5" style="text-align:center; padding: 40px; color: var(--mo-muted);">
                                <i class="fas fa-shopping-bag" style="font-size: 1.5rem; margin-bottom: 10px; display: block; opacity: 0.5;"></i>
                                Non hai ancora effettuato nessun ordine. <br>
                                <a href="${pageContext.request.contextPath}/CatalogServlet" style="color: var(--mo-orange); text-decoration: underline; margin-top:10px; display:inline-block;">Vai al Catalogo</a>
                            </td>
                        </tr>
                    <% } else { 
                        for (OrderBean order : orders) { %>
                            <tr>
                                <td class="mo-id-cell">#<%= order.getId() %></td>
                                <td><%= order.getCreatedAt() != null ? order.getCreatedAt().format(fmt) : "N/D" %></td>
                                <td><span class="mo-status-badge"><%= getStatusLabel(order.getStatus().name()) %></span></td>
                                <td style="text-align: right; font-weight: 700; color: var(--mo-orange);">
                                    &euro; <%= String.format("%.2f", order.getTotalAmount()) %>
                                </td>
                                <td class="mo-action-cell">
                                    <button class="mo-btn-details" onclick="toggleDetails('mo-row-<%= order.getId() %>')">
                                        <i class="fas fa-eye"></i> Dettagli
                                    </button>
                                    <a href="${pageContext.request.contextPath}/DownloadInvoiceServlet?orderId=<%= order.getId() %>" class="mo-btn-invoice" target="_blank" title="Scarica il PDF della fattura">
                                        <i class="fas fa-file-pdf"></i> Fattura
                                    </a>
                                </td>
                            </tr>
                            
                            <tr id="mo-row-<%= order.getId() %>" class="mo-details-row" style="display: none;">
                                <td colspan="5" style="padding: 15px 30px;">
                                    <% if (order.getItems() == null || order.getItems().isEmpty()) { %>
                                        <p style="font-style: italic; color: var(--mo-muted);">Nessun prodotto disponibile.</p>
                                    <% } else { %>
                                        <table class="mo-inner-table">
                                            <thead>
                                                <tr>
                                                    <th>Prodotto</th>
                                                    <th>Q.tà</th>
                                                    <th>Prezzo cad.</th>
                                                    <th>IVA</th>
                                                    <th>Subtotale</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% for (OrderItemBean item : order.getItems()) { 
                                                    // Ricerca del nome prodotto in base al productId della variante
                                                    String productName = "Prodotto non trovato";
                                                    if (products != null && item.getVariant() != null) {
                                                        for (ProductBean p : products) {
                                                            if (p.getId() == item.getVariant().getProductId()) {
                                                                productName = p.getName();
                                                                break;
                                                            }
                                                        }
                                                    }
                                                %>
                                                    <tr>
                                                        <td>
                                                            <strong><%= productName %></strong> 
                                                            <span style="font-size:0.8rem; color:var(--mo-muted);">(Var. #<%= item.getVariant().getId() %>)</span>
                                                        </td>
                                                        <td><%= item.getQuantity() %></td>
                                                        <td>€ <%= String.format("%.2f", item.getPriceAtPurchase()) %></td>
                                                        <td><%= String.format("%.0f", item.getVat()) %>%</td>
                                                        <td>€ <%= String.format("%.2f", item.getPriceAtPurchase() * item.getQuantity()) %></td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    <% } %>
                                </td>
                            </tr>
                    <%  } 
                    } %>
                </tbody>
            </table>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/fragment/footer.jsp" %>
<script src="${pageContext.request.contextPath}/js/myorders.js" defer></script>
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
