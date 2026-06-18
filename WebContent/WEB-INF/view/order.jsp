<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.backend.model.bean.CartBean" %>
<%@ page import="it.unisa.backend.model.bean.CartItemBean" %>
<%@ page import="it.unisa.backend.model.bean.AddressBean" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%
    CartBean cart = (CartBean) request.getAttribute("cart");
    String estimatedDelivery = (String) request.getAttribute("estimatedDelivery");
    List<AddressBean> userAddresses = (List<AddressBean>) request.getAttribute("userAddresses");
    
    double cartSubtotal = cart != null ? cart.getTotalPrice() : 0.0;
    double shippingCost = (cartSubtotal >= 50.0 || cartSubtotal == 0.0) ? 0.0 : 5.90;
    double orderTotal = cartSubtotal + shippingCost;
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Riepilogo Ordine - IronAxis</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/fragment/header.jsp" />
    <jsp:include page="/WEB-INF/fragment/menu.jsp" />

    <main class="checkout-container">
        <div class="checkout-header">
            <h2>Riepilogo dell'ordine</h2>
            <p>Controlla i tuoi dati, seleziona l'indirizzo e verifica gli articoli prima di procedere al pagamento sicuro.</p>
        </div>

        <div class="checkout-grid">
            
            <div class="checkout-main">
                
                <section class="info-section-standard">
                    <h3><i class="fa-solid fa-location-dot"></i> Scegli l'Indirizzo di Spedizione</h3>
                    
                    <form id="checkoutForm" action="<%= request.getContextPath() %>/PaymentServlet" method="GET">
                        <div class="address-grid">
                            <%
                                if (userAddresses != null && !userAddresses.isEmpty()) {
                                    int index = 0;
                                    for (AddressBean addr : userAddresses) {
                                        String checked = (index == 0) ? "checked" : "";
                            %>
                                        <label class="address-card">
                                            <input type="radio" name="selectedAddressId" value="<%= addr.getId() %>" <%= checked %>>
                                            <div class="address-card-content">
                                                <span class="address-alias"><strong>Indirizzo #<%= index + 1 %></strong></span>
                                                <span class="address-text">
                                                    <%= addr.getStreet() %>, <%= addr.getStreetNumber() %><br>
                                                    <%= addr.getZipCode() %> - <%= addr.getCity() %> (<%= addr.getProvince() %>)<br>
                                                    <%= addr.getCountry() %>
                                                </span>
                                            </div>
                                        </label>
                            <%
                                        index++;
                                    }
                                } else {
                            %>
                                <div class="no-address-alert">
                                    <p><i class="fa-solid fa-triangle-exclamation"></i> Non hai ancora salvato nessun indirizzo di spedizione.</p>
                                </div>
                            <%
                                }
                            %>
                        </div>
                    </form>
                        
                    <% if (userAddresses != null && !userAddresses.isEmpty()) { %>
                        <div class="add-address-shortcut">
                            <button type="button" id="toggleAddressFormBtn" class="btn-text">
                                <i class="fa-solid fa-plus"></i> Aggiungi un nuovo indirizzo
                            </button>
                        </div>
                    <% } %>

                    <div id="inlineAddressFormContainer" class="inline-form-container is-hidden">
                        <h4>Inserisci Nuovo Indirizzo</h4>
                        <form id="ajaxAddAddressForm" data-context="<%= request.getContextPath() %>">
                            <input type="hidden" name="action" value="add">

                            <div class="form-row">
                                <input type="text" name="street" placeholder="Via/Piazza (es. Via Roma)" required> 
                                <input type="text" name="streetNumber" placeholder="Civico" required class="input-small">
                            </div>
                            <div class="form-row">
                                <input type="text" name="city" placeholder="Città" required>
                                <input type="text" name="zipCode" placeholder="CAP" required class="input-small">
                            </div>
                            <div class="form-row">
                                <input type="text" name="province" placeholder="Provincia" required> 
                                <input type="text" name="country" placeholder="Nazione" required>
                            </div>

                            <div id="addressFormError" class="error-text is-hidden"></div>

                            <div class="form-actions">
                                <button type="submit" class="btn-secondary">Salva e Seleziona</button>
                                <button type="button" id="cancelAddressFormBtn" class="btn-text btn-cancel">Annulla</button>
                            </div>
                        </form>
                    </div>
                    
                    <div class="info-box-delivery">
                        <h3><i class="fa-solid fa-truck"></i> Consegna Stimata</h3>
                        <p class="delivery-date">
                            <i class="fa-regular fa-calendar-check"></i> 
                            <%= estimatedDelivery != null ? estimatedDelivery : "In 3-5 giorni lavorativi" %>
                        </p>
                    </div>
                </section>

                <section class="products-section">
                    <h3>Articoli nel tuo ordine (<%= cart != null && cart.getVariants() != null ? cart.getVariants().size() : 0 %>)</h3>
                    <div class="order-items-list">
                        <%
                            if (cart != null && cart.getVariants() != null && !cart.getVariants().isEmpty()) {
                                for (Map.Entry<Long, CartItemBean> entry : cart.getVariants().entrySet()) {
                                    CartItemBean item = entry.getValue();
                                    double unitPrice = item.getVariant().getPrice();
                                    double subTotal = unitPrice * item.getSelectedQuantity();
                        %>
                                    <div class="order-item">
                                        <img src="<%= request.getContextPath() %>/<%= item.getVariant().getImageUrl() %>" alt="Immagine Variante" class="item-img">
                                        
                                        <div class="item-details">
                                            <h4 class="item-title"><%= item.getVariant().getSku() %></h4>
                                            <p class="item-variants">
                                                <span><strong>Gusto:</strong> <%= item.getVariant().getFlavour() != null ? item.getVariant().getFlavour() : "N/D" %></span> | 
                                                <span><strong>Formato:</strong> <%= item.getVariant().getSize() != null ? item.getVariant().getSize() : "N/D" %></span>
                                                <span><strong>IVA:</strong> <%= item.getVariant().getVat() %>%</span>
                                            </p>
                                            <p class="item-price">
                                                <%= item.getSelectedQuantity() %> x <span><%= String.format(java.util.Locale.US, "%.2f", unitPrice) %> €</span>
                                            </p>
                                        </div>
                                        
                                        <div class="item-subtotal">
                                            <span><%= String.format(java.util.Locale.US, "%.2f", subTotal) %> €</span>
                                        </div>
                                    </div>
                                    <hr class="item-divider">
                        <%
                                }
                            } else {
                        %>
                                <div class="empty-checkout-alert">
                                    <p>Il tuo carrello è vuoto. Non ci sono articoli da processare.</p>
                                    <a href="<%= request.getContextPath() %>/index.jsp" class="btn-secondary">Torna allo Shop</a>
                                </div>
                        <%
                            }
                        %>
                    </div>
                </section>
            </div>

            <aside class="checkout-sidebar">
                <div class="summary-card">
                    <h3>Riepilogo Costi</h3>
                    
                    <div class="summary-row">
                        <span>Subtotale Prodotti</span>
                        <span><%= String.format(java.util.Locale.US, "%.2f", cartSubtotal) %> €</span>
                    </div>          
                    
                    <div class="summary-row">
                        <span>Spedizione</span>
                        <span class="<%= shippingCost == 0.0 ? "shipping-free" : "" %>">
                            <%= shippingCost == 0.0 ? "Gratis" : String.format(java.util.Locale.US, "%.2f", shippingCost) + " €" %>
                        </span>
                    </div>
                    
                    <% if (shippingCost > 0.0) { %>
                        <p class="shipping-hint">Aggiungi altre <span><%= String.format(java.util.Locale.US, "%.2f", (50.0 - cartSubtotal)) %> €</span> di prodotti per avere la spedizione <strong>Gratis</strong>!</p>
                    <% } %>
                    
                    <hr>
                    
                    <div class="summary-row total-row">
                        <span>Totale dell'ordine</span>
                        <span class="final-price"><%= String.format(java.util.Locale.US, "%.2f", orderTotal) %> €</span>
                    </div>
                    
                    <p class="tax-include-text">Tutti i prezzi sono comprensivi di IVA applicabile.</p>

                    <div class="action-bar">
                        <button type="submit" form="checkoutForm" class="btn-pay" <%= (cart == null || cart.getVariants() == null || cart.getVariants().isEmpty() || userAddresses == null || userAddresses.isEmpty()) ? "disabled" : "" %>>
                            <i class="fa-solid fa-lock"></i> Paga in Sicurezza
                        </button>
                    </div>
                    
                    <div class="secure-checkout-footer">
                        <p class="secure-text"><i class="fa-solid fa-shield-halved"></i> Transazione crittografata a 256-bit.</p>
                        <div class="payment-icons">
                            <i class="fa-brands fa-cc-visa"></i>
                            <i class="fa-brands fa-cc-mastercard"></i>
                            <i class="fa-brands fa-cc-paypal"></i>
                            <i class="fa-brands fa-cc-apple-pay"></i>
                        </div>
                    </div>
                </div>
            </aside>
            
        </div>
    </main>

    <jsp:include page="/WEB-INF/fragment/footer.jsp" />
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/order.js" defer></script>

</body>
</html>