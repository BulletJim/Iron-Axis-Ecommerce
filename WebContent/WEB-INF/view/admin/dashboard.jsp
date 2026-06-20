<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, it.unisa.backend.model.bean.ProductBean, it.unisa.backend.model.bean.OrderBean, it.unisa.backend.model.bean.CategoryBean" %>
<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
    ProductBean editProduct = (ProductBean) request.getAttribute("productToEdit");
    boolean isEdit = (editProduct != null);
    
    List<OrderBean> ordersList = (List<OrderBean>) request.getAttribute("orders");
    String activePanel = (String) request.getAttribute("activePanel");
    
    if (activePanel == null) {
        activePanel = "panel-add";
    }

    @SuppressWarnings("unchecked")
    List<CategoryBean> adminGlobalCategories = (List<CategoryBean>) application.getAttribute("globalCategories");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Console Admin - Iron Axis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="admin-body" data-active-panel="<%= activePanel %>">

    <%@ include file="/WEB-INF/fragment/header.jsp" %>
    <%@ include file="/WEB-INF/fragment/menu.jsp" %>

    <main class="admin-wrapper">
        <div class="hub-header">
            <h1>Pannello di controllo amministrativo</h1>
            <p>Seleziona una delle tre operazioni principali per gestire il catalogo Iron Axis.</p>
        </div>

        <div class="hub-navigation">
            <button class="hub-btn <%= "panel-add".equals(activePanel) ? "active" : "" %>" onclick="switchTab('panel-add')">
                <i class="fas <%= isEdit ? "fa-pen-square" : "fa-plus-circle" %>"></i>
                <span><%= isEdit ? "Modifica Prodotto" : "Nuovo Prodotto" %></span>
            </button>
            <button class="hub-btn <%= "panel-variant".equals(activePanel) ? "active" : "" %>" onclick="switchTab('panel-variant')">
                <i class="fas fa-cubes"></i>
                <span>Aggiungi variante</span>
            </button>
            <button class="hub-btn <%= "panel-list".equals(activePanel) ? "active" : "" %>" onclick="switchTab('panel-list')">
                <i class="fas fa-list-alt"></i>
                <span>Elenco & Modifiche</span>
            </button>
            <button class="hub-btn <%= "panel-orders".equals(activePanel) ? "active" : "" %>" onclick="switchTab('panel-orders')">
                <i class="fas fa-box-open"></i>
                <span>Storico ordini</span>
            </button>
        </div>

        <div id="panel-add" class="hub-panel <%= "panel-add".equals(activePanel) ? "active" : "" %>">
            <div class="admin-card">
                <h2><%= isEdit ? "Modifica Prodotto Base (ID: #" + editProduct.getId() + ")" : "Inserimento Nuovo Prodotto Base" %></h2>
                <form id="productForm" action="${pageContext.request.contextPath}/admin/AdminProductServlet" method="POST" class="admin-form" novalidate>
                    <input type="hidden" name="action" value="save">
       
                    <% if(isEdit) { %>
                        <input type="hidden" name="id" value="<%= editProduct.getId() %>">
                    <% } %>

                    <div class="form-group">
                        <label for="name">Nome prodotto *</label>
                        <input type="text" id="name" name="name" value="<%= isEdit ? editProduct.getName() : "" %>" placeholder="Es: Iso Whey Zero" required>
                    </div>

                    <div class="form-group">
                        <label for="categoryId">Categoria del Prodotto *</label>
                        <select id="categoryId" name="categoryId" required>
                            <option value="" disabled <%= !isEdit ? "selected" : "" %>>-- Seleziona Categoria --</option>
                            <% 
                                if (adminGlobalCategories != null) {
                                    for (CategoryBean category : adminGlobalCategories) {
                                        boolean isSelected = (isEdit && editProduct.getCategory() != null && editProduct.getCategory().getId() == category.getId());
                            %>
                                <option value="<%= category.getId() %>" <%= isSelected ? "selected" : "" %>>
                                    <%= category.getName() %> (Macro: <%= category.getMacroCategory() %>)
                                </option>
                            <% 
                                    }
                                } 
                            %>
                        </select>
                    </div>

                    <div class="form-group full-width">
                        <label for="description">Descrizione tecnica *</label>
                        <textarea id="description" name="description" rows="4" placeholder="Fornisci i dettagli commerciali e tecnici del prodotto..." required><%= isEdit ? editProduct.getDescription() : "" %></textarea>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-submit"><%= isEdit ? "Salva Modifiche" : "Crea Prodotto" %></button>
                        <% if(isEdit) { %>
                            <a href="${pageContext.request.contextPath}/AdminProductServlet" class="btn-cancel">Annulla modifica</a>
                        <% } %>
                    </div>
                </form>
            </div>
        </div>

        <div id="panel-variant" class="hub-panel <%= "panel-variant".equals(activePanel) ? "active" : "" %>">
            <div class="admin-card">
                <h2>Associa variante specifiche a un prodotto</h2>
                <form id="variantForm" action="${pageContext.request.contextPath}/admin/AdminProductServlet" method="POST" enctype="multipart/form-data" class="admin-form" novalidate>
                    <input type="hidden" name="action" value="addVariant">

                    <div class="form-group">
                        <label for="variantProductId">Seleziona prodotto madre *</label>
                        <select id="variantProductId" name="productId" required>
                            <option value="" disabled selected>-- Scegli a quale prodotto associarla --</option>
                            <% if(products != null) { 
                                for(ProductBean p : products) { %>
                                    <option value="<%= p.getId() %>"><%= p.getName() %> (ID: <%= p.getId() %>)</option>
                            <% } } %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="sku">Codice SKU *</label>
                        <input type="text" id="sku" name="sku" placeholder="Es: WH-CHO-1000" required>
                    </div>

                    <div class="form-group">
                        <label for="flavour">Gusto / Variante Aromatica</label>
                        <input type="text" id="flavour" name="flavour" placeholder="Es: Cioccolato Fondente / Neutro">
                    </div>

                    <div class="form-group">
                        <label for="size">Formato / Taglia</label>
                        <input type="text" id="size" name="size" placeholder="Es: 1000g / 90 Capsule">
                    </div>

                    <div class="form-group">
                        <label for="price">Prezzo (€) *</label>
                        <input type="number" id="price" name="price" step="0.01" placeholder="Es: 39.99" required>
                    </div>

                    <div class="form-group">
                        <label for="vat">Aliquota IVA (%) *</label>
                        <input type="number" id="vat" name="vat" step="0.1" value="22.0" placeholder="Es: 22.0" required>
                    </div>

                    <div class="form-group">
                        <label for="quantity">Quantità in magazzino *</label>
                        <input type="number" id="quantity" name="quantity" placeholder="Es: 50" required>
                    </div>

                    <div class="form-group">
                        <label for="productImage">File Immagine prodotto</label>
                        <input type="file" id="productImage" name="productImage" accept="image/jpeg, image/png">
                    </div>

                    <div class="form-group">
                        <label for="nutritionalTable">File Tabella nutrizionale</label>
                        <input type="file" id="nutritionalTable" name="nutritionalTable" accept="image/jpeg, image/png, application/pdf">
                    </div>

                    <div class="form-actions full-width">
                        <button type="submit" class="btn-submit">Salva ed associa variante</button>
                    </div>
                </form>
            </div>
        </div>

        <div id="panel-list" class="hub-panel <%= "panel-list".equals(activePanel) ? "active" : "" %>">
            <div class="admin-card">
                <div class="table-header-flex">
                    <h2>Catalogo generale prodotti registrati</h2>

                    <div class="filter-wrapper">
                        <label for="tableCategoryFilter"><i class="fas fa-filter"></i> Filtra categoria:</label>
                        <select id="tableCategoryFilter" onchange="filterTableByCategory()">
                            <option value="all">Mostra Tutte</option>
                            <% 
                                if (adminGlobalCategories != null) {
                                    for (CategoryBean category : adminGlobalCategories) {
                            %>
                                <option value="<%= category.getId() %>"><%= category.getName() %></option>
                            <% 
                                    }
                                } 
                            %>
                        </select>
                    </div>
                </div>

                <div class="table-container">
                    <table class="admin-table" id="productsTable">
                        <thead>
                            <tr>
                                <th>ID Auto</th>
                                <th>Nome prodotto</th>
                                <th>Categoria collegata</th>
                                <th style="text-align: center;">Azioni correnti</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if(products != null && !products.isEmpty()) { 
                                for(ProductBean p : products) { 
                                    long catId = p.getCategory() != null ? p.getCategory().getId() : 0;
                                    String catLabel = "Sconosciuta";
                                    
                                    if (adminGlobalCategories != null) {
                                        for(CategoryBean c : adminGlobalCategories) {
                                            if(c.getId() == catId) {
                                                catLabel = c.getName();
                                                break;
                                            }
                                        }
                                    }
                                %>
                                <tr class="product-row" data-category-id="<%= catId %>">
                                    <td class="id-cell">#<%= p.getId() %></td>
                                    <td><strong><%= p.getName() %></strong></td>
                                    <td>
                                        <span class="category-badge cat-<%= catId %>">
                                             <%= catLabel %> (ID: <%= catId %>)
                                        </span>
                                    </td>
                                    <td style="text-align: center;">
                                        <a href="${pageContext.request.contextPath}/admin/AdminProductServlet?action=edit&id=<%= p.getId() %>" class="btn-edit">
                                            <i class="fas fa-pen"></i> Modifica
                                        </a>
                                        <form action="${pageContext.request.contextPath}/admin/AdminProductServlet" method="POST" style="display:inline;" onsubmit="return confirm('Vuoi davvero rimuovere questo prodotto?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="<%= p.getId() %>">
                                            <button type="submit" class="btn-delete">
                                                <i class="fas fa-trash-alt"></i> Rimuovi
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            <% } } else { %>
                                <tr><td colspan="4" style="text-align:center; padding: 30px;">Nessun prodotto base nel database.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div id="panel-orders" class="hub-panel <%= "panel-orders".equals(activePanel) ? "active" : "" %>">
            <div class="admin-card">
                <h2>Filtra e monitora lo storico ordini</h2>
                <p style="color: var(--iron-muted); font-size: 0.9rem; margin-bottom: 25px;">Utilizza i filtri combinati sottostanti per analizzare e ispezionare gli ordini effettuati dai clienti della piattaforma.</p>
                
                <form id="orderFilterForm" action="${pageContext.request.contextPath}/admin/AdminOrderServlet" method="GET" class="admin-form" novalidate>
                    <div class="form-group">
                        <label for="startDate">Data inizio intervallo</label>
                        <input type="date" id="startDate" name="startDate" placeholder="Seleziona la data iniziale">
                    </div>
                    <div class="form-group">
                        <label for="endDate">Data fine intervallo</label>
                        <input type="date" id="endDate" name="endDate" placeholder="Seleziona la data finale">
                    </div>
                    <div class="form-group full-width">
                        <label for="customerQuery">Filtro cliente (Email, Nome o Cognome)</label>
                        <input type="text" id="customerQuery" name="customerQuery" placeholder="Inserisci parte dell'email o del nome del cliente per effettuare la ricerca...">
                    </div>
                    
                    <span id="orderFilterError" class="error-inline"></span>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn-submit"><i class="fas fa-filter"></i> Applica filtri</button>
                        <a href="${pageContext.request.contextPath}/admin/AdminOrderServlet" class="btn-cancel"><i class="fas fa-undo"></i> Ripristina</a>
                    </div>
                </form>

                <div class="table-container">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID ordine</th>
                                <th>Data transazione</th>
                                <th>Email utente</th>
                                <th style="text-align: right;">Totale transato</th>
                                <th style="text-align: center;">Fattura</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                            if (ordersList != null && !ordersList.isEmpty()) { 
                                for(OrderBean o : ordersList) { 
                            %>
                                    <tr>
                                        <td><strong>#<%= o.getId() %></strong></td>
                                        <td><%= o.getCreatedAt() != null ? o.getCreatedAt().toLocalDate() : "N/A" %></td>
                                        <td><%= o.getUser() != null ? o.getUser().getEmail() : "N/A" %></td>
                                        <td style="text-align: right; font-weight: 700; color: var(--iron-orange);">&euro; <%= String.format("%.2f", o.getTotalAmount()) %></td>
                                        <td class="action-cell">
                                            <a href="${pageContext.request.contextPath}/DownloadInvoiceServlet?orderId=<%= o.getId() %>" class="btn-invoice" title="Scarica la fattura in formato PDF">
                                                 Scarica PDF
                                            </a>
                                        </td>
                                    </tr>
                            <%  } 
                            } else { %>
                                <tr>
                                    <td colspan="5" style="text-align:center; padding: 40px; color: var(--iron-muted);">
                                        <i class="fas fa-search" style="font-size: 1.5rem; margin-bottom: 10px; display: block; opacity: 0.5;"></i>
                                        Nessun ordine trovato. Inizializza o modifica i filtri di ricerca.
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
    </main>

    <%@ include file="/WEB-INF/fragment/footer.jsp" %>

    <script src="${pageContext.request.contextPath}/js/admin-dashboard.js" defer></script>
</body>
</html>