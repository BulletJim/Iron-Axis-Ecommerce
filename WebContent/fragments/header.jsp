<header class="site-header">
    <button class="menu-btn" id="menuToggle" aria-label="Apri menu">
        <i class="fas fa-bars"></i>
    </button>
    <a href="${pageContext.request.contextPath}/index.jsp" class="logo">IRON AXIS</a>

    <form action="${pageContext.request.contextPath}/SearchServlet" method="get" class="search-bar">
        <input type="text" name="query" id="searchBox" placeholder="Cerca integratori, accessori..." autocomplete="off">
        <button type="submit" aria-label="Cerca">
            <i class="fas fa-search"></i> </button>
        <div id="searchSuggestions" class="suggestions-box"></div>
    </form>

    <div class="header-actions">
        <a href="${pageContext.request.contextPath}/login.jsp" class="icon-btn" title="Login">
            <i class="fas fa-user"></i>
        </a>

        <a href="${pageContext.request.contextPath}/CartServlet" class="icon-btn" title="Carrello">
            <i class="fas fa-shopping-cart"></i>
        </a>
    </div>
</header>