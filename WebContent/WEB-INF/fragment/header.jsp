<%@ page import="it.unisa.backend.model.bean.UserBean" %>
<%
    // Recuperiamo l'utente dalla sessione
    UserBean loggedUser = (UserBean) session.getAttribute("loggedUser");
%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/account.css">

<header class="site-header">
    
    <%
    	String successMessage = (String) session.getAttribute("successMessage");
    	if (successMessage != null && !successMessage.isEmpty()) {
	%>
    <div class="toast-banner toast-success" id="toast-message">
        <%= successMessage %>
    </div>
	<%
        	session.removeAttribute("successMessage");
    	}
	%>

	<%
    	String errorMessage = (String) session.getAttribute("errorMessage");
    	if (errorMessage != null && !errorMessage.isEmpty()) {
	%>
    <div class="toast-banner toast-error" id="toast-message">
        <%= errorMessage %>
    </div>
	<%
        	session.removeAttribute("errorMessage");
    	}
	%>
    
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
        <% if (loggedUser != null) { %>
            <div class="user-dropdown">
                <button class="icon-btn dropbtn" title="Ciao, <%= loggedUser.getFirstName() %>">
                    <i class="fas fa-user-check"></i>
                </button>
                
                <div class="dropdown-content">
                    <div class="dropdown-welcome">Ciao, <strong><%= loggedUser.getFirstName() %></strong></div>
                    
                    <a href="${pageContext.request.contextPath}/ProfileServlet">
                        <i class="fas fa-user-cog"></i> Il mio Profilo
                    </a>
                    
                    <% if ("admin".equalsIgnoreCase(loggedUser.getRole())) { %>
                        <a href="${pageContext.request.contextPath}/AdminProductServlet" class="admin-link">
                            <i class="fas fa-cogs"></i> Gestione sito (Admin)
                        </a>
                    <% } %>
                    
                    <div class="dropdown-divider"></div>
                    
                    <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout-link">
                        <i class="fas fa-sign-out-alt"></i> Esci
                    </a>
                </div>
            </div>

        <% } else { %>
            <a href="${pageContext.request.contextPath}/LoginServlet" class="icon-btn" title="Login o Registrati">
                <i class="fas fa-user"></i>
            </a>
        <% } %>

        <a href="${pageContext.request.contextPath}/CartServlet" class="icon-btn" title="Carrello">
            <i class="fas fa-shopping-cart"></i>
        </a>
    </div>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/toastBannerController.js" defer></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/searchbar.js" defer></script>
</header>



