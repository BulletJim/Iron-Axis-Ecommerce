<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    java.util.List<String> footerMacros = new java.util.ArrayList<>();
    java.util.List<Long> footerFirstIds = new java.util.ArrayList<>();
    
    @SuppressWarnings("unchecked")
    java.util.List<it.unisa.backend.model.bean.CategoryBean> footerGlobalCategories = 
        (java.util.List<it.unisa.backend.model.bean.CategoryBean>) application.getAttribute("globalCategories");
        
    if (footerGlobalCategories != null) {
        for (it.unisa.backend.model.bean.CategoryBean cat : footerGlobalCategories) {
            String macro = cat.getMacroCategory();
            
            if (macro != null && !footerMacros.contains(macro)) {
                footerMacros.add(macro);
                footerFirstIds.add(cat.getId());
            }
        }
    }
%>

<footer class="site-footer">
    <div class="footer-container">
        <div class="footer-section">
            <h3>Chi Siamo</h3>
            <p>Siamo leader nella vendita di integratori<br>e attrezzatura sportiva di altissima qualità.<br>Raggiungi i
                tuoi obiettivi con Iron Axis.</p>
        </div>

        <div class="footer-section">
            <h3>Prodotti</h3>
            <ul>
                <% 
                    if (!footerMacros.isEmpty()) {
                        for (int i = 0; i < footerMacros.size(); i++) { 
                %>
                    <li>
                        <a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=<%= footerFirstIds.get(i) %>">
                            <%= footerMacros.get(i) %>
                        </a>
                    </li>
                <% 
                        }
                    } else { 
                %>
                    <li><a href="#">Catalogo in aggiornamento</a></li>
                <%  } %>
            </ul>
        </div>

        <div class="footer-section">
            <h3>Contatti</h3>
            <ul>
                <li><a href="mailto:info@ironaxis.it">info@ironaxis.it</a></li>
                <li><a href="tel:+390123456789">+39 012 345 6789</a></li>
                <li><a href="${pageContext.request.contextPath}/assistance.jsp">Assistenza Clienti</a></li>
                <li><a href="${pageContext.request.contextPath}/ordersinfo.jsp">Spedizioni & Resi</a></li>
            </ul>
        </div>
    </div>

    <div class="footer-bottom">
        <div class="copyright">
            &copy; 2026 Iron Axis. Tutti i diritti riservati.
        </div>
        <div class="payment-methods">
            <i class="fab fa-cc-visa" title="Visa"></i>
            <i class="fab fa-cc-mastercard" title="Mastercard"></i>
            <i class="fab fa-cc-amex" title="American Express"></i>
            <i class="fab fa-cc-paypal" title="PayPal"></i>
            <i class="fab fa-cc-apple-pay" title="Apple Pay"></i>
        </div>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/menu.js"></script>