<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, it.unisa.backend.model.bean.ProductBean" %>
<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("products");
    ProductBean editProduct = (ProductBean) request.getAttribute("productToEdit");
    boolean isEdit = (editProduct != null);
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
<body class="admin-body">

    <%@ include file="/WEB-INF/fragment/header.jsp" %>
    <%@ include file="/WEB-INF/fragment/menu.jsp" %>

    <main class="admin-wrapper">
        <div class="hub-header">
            <h1>Pannello di controllo amministrativo</h1>
            <p>Seleziona una delle tre operazioni principali per gestire il catalogo Iron Axis.</p>
        </div>

        <div class="hub-navigation">
            <button class="hub-btn active" onclick="switchTab('panel-add')">
                <i class="fas <%= isEdit ? "fa-pen-square" : "fa-plus-circle" %>"></i>
                <span><%= isEdit ? "Modifica Prodotto" : "Nuovo Prodotto" %></span>
            </button>
            <button class="hub-btn" onclick="switchTab('panel-variant')">
                <i class="fas fa-cubes"></i>
                <span>Aggiungi variante</span>
            </button>
            <button class="hub-btn" onclick="switchTab('panel-list')">
                <i class="fas fa-list-alt"></i>
                <span>Elenco & Modifiche</span>
            </button>
        </div>

        <div id="panel-add" class="hub-panel active">
            <div class="admin-card">
                <h2><%= isEdit ? "Modifica Prodotto Base (ID: #" + editProduct.getId() + ")" : "Inserimento Nuovo Prodotto Base" %></h2>
                <form id="productForm" action="${pageContext.request.contextPath}/AdminProductServlet" method="POST" class="admin-form" novalidate>
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
                            <option value="1" <%= (isEdit && editProduct.getCategory().getId() == 1) ? "selected" : "" %>>Proteine (ID: 1)</option>
                            <option value="2" <%= (isEdit && editProduct.getCategory().getId() == 2) ? "selected" : "" %>>Energia & Resistenza (ID: 2)</option>
                            <option value="3" <%= (isEdit && editProduct.getCategory().getId() == 3) ? "selected" : "" %>>Vitamine e Macronutrienti (ID: 3)</option>
                            <option value="4" <%= (isEdit && editProduct.getCategory().getId() == 4) ? "selected" : "" %>>Accessori (ID: 4)</option>
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

        <div id="panel-variant" class="hub-panel">
            <div class="admin-card">
                <h2>Associa variante specifiche a un prodotto</h2>
                <form id="variantForm" action="${pageContext.request.contextPath}/AdminProductServlet" method="POST" enctype="multipart/form-data" class="admin-form" novalidate>
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

        <div id="panel-list" class="hub-panel">
            <div class="admin-card">
                <div class="table-header-flex">
                    <h2>Catalogo generale prodotti registrati</h2>
                    
                    <div class="filter-wrapper">
                        <label for="tableCategoryFilter"><i class="fas fa-filter"></i> Filtra categoria:</label>
                        <select id="tableCategoryFilter" onchange="filterTableByCategory()">
                            <option value="all">Mostra Tutte</option>
                            <option value="1">Proteine (ID: 1)</option>
                            <option value="2">Energia & Resistenza (ID: 2)</option>
                            <option value="3">Vitamine e Macronutrienti (ID: 3)</option>
                            <option value="4">Accessori (ID: 4)</option>
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
                                    long catId = p.getCategory().getId();
                                    String catLabel = "Altro";
                                    if(catId == 1) catLabel = "Proteine";
                                    else if(catId == 2) catLabel = "Energia & Resistenza";
                                    else if(catId == 3) catLabel = "Vitamine e Macro";
                                    else if(catId == 4) catLabel = "Accessori";
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
                                        <a href="${pageContext.request.contextPath}/AdminProductServlet?action=edit&id=<%= p.getId() %>" class="btn-edit">
                                            <i class="fas fa-pen"></i> Modifica
                                        </a>
                                        <form action="${pageContext.request.contextPath}/AdminProductServlet" method="POST" style="display:inline;" onsubmit="return confirm('Vuoi davvero rimuovere questo prodotto?');">
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
    </main>

    <%@ include file="/WEB-INF/fragment/footer.jsp" %>
    
    <script src="${pageContext.request.contextPath}/js/admin-dashboard.js" defer></script>
</body>
</html>