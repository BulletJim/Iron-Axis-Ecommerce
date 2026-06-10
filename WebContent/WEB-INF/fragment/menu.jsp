<div id="sidebar-backdrop" class="sidebar-backdrop"></div>   

<nav class="sidebar" id="sidebar">
    <div class="sidebar-header">
        <span class="sidebar-title">Menu</span>
        <button id="sidebar-close-btn" class="sidebar-close-btn" aria-label="Chiudi menu">&times;</button> 
    </div>

    <ul class="sidebar-menu">
        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Proteine<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=proteine-latte">Proteine siero del latte</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=proteine-vegetali">Proteine vegetali</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=proteine-caseine">Caseine</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=proteine-barrette">Barrette proteiche</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Energia & Resistenza<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=energia-pre-workout">Pre-workout</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=energia-intra-workout">Intra-workout</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=energia-post-workout">Post-workout</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=energia-creatina">Creatina</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=energia-amminoacidi">Amminoacidi</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Vitamine e Macronutrienti<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=vitamine-vitamine-minerali">Vitamine e minerali</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=vitamine-acidi-grassi">Acidi grassi</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=vitamine-sonno-recupero">Sonno e recupero</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Obiettivi<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=obiettivi-crescita-muscolare">Crescita muscolare</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=obiettivi-dimagrimento">Dimagrimento</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=obiettivi-resistenza">Resistenza</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Accessori<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=accessori-attrezzatura-supporti">Attrezzatura e supporti</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=accessori-abbigliamento-uomo">Abbigliamento Uomo</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?category=accessori-abbigliamento-donna">Abbigliamento Donna</a></li>
            </ul>
        </li>
    </ul>
</nav>