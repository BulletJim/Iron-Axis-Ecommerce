<header class="site-header">
    <button class="menu-btn"></button>
    <a href="index.jsp" class="logo">IRON AXIS</a>

    <form action="${pageContext.request.contextPath}/SearchServlet" method="get" class="search-bar">
        <input type="text" name="cerca" id="cerca" placeholder="Cerca integratori, accessori...">
        <button type="submit"></button>
    </form>

    <a href="login.jsp" class="icon-btn" title="Login"></a>
    <a href="cart.jsp" class="icon-btn" title="Cart"></a>
</header>