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
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=1">Proteine siero del latte</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=2">Proteine vegetali</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=3">Caseine</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=4">Barrette proteiche</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Energia & Resistenza<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=5">Pre-workout</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=6">Intra-workout</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=7">Post-workout</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=8">Creatina</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=9">Amminoacidi</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Vitamine e Macronutrienti<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=10">Vitamine e minerali</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=11">Acidi grassi</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=12">Sonno e recupero</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Obiettivi<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=13">Crescita muscolare</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=14">Dimagrimento</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=15">Resistenza</a></li>
            </ul>
        </li>

        <li class="menu-item has-submenu">
            <a href="#" class="submenu-toggle">Accessori<i class="fas fa-chevron-down arrow"></i></a>
            <ul class="submenu">
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=16">Attrezzatura e supporti</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=17">Abbigliamento Uomo</a></li>
                <li><a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=18">Abbigliamento Donna</a></li>
            </ul>
        </li>
    </ul>
</nav>