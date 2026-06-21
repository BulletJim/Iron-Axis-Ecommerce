<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="it.unisa.backend.model.bean.CategoryBean" %>

<%
    @SuppressWarnings("unchecked")
    List<CategoryBean> globalCategories = (List<CategoryBean>) application.getAttribute("globalCategories");

    List<String> macroList = new ArrayList<>();
    if (globalCategories != null) {
        for (CategoryBean cat : globalCategories) {
            String macro = cat.getMacroCategory();
            if (macro != null && !macroList.contains(macro)) {
                macroList.add(macro);
            }
        }
    }
%>

<div id="sidebar-backdrop" class="sidebar-backdrop"></div>   

<nav class="sidebar" id="sidebar">
    <div class="sidebar-header">
        <span class="sidebar-title"></span>
        <button id="sidebar-close-btn" class="sidebar-close-btn" aria-label="Chiudi menu">&times;</button> 
    </div>

    <ul class="sidebar-menu">
        <% 
            if (!macroList.isEmpty()) {
                for (String macroName : macroList) { 
        %>
            <li class="menu-item has-submenu">
                <a href="#" class="submenu-toggle"><%= macroName %><i class="fas fa-chevron-down arrow"></i></a>
                <ul class="submenu">
                    <% 
                        for (CategoryBean category : globalCategories) { 
                            if (macroName.equals(category.getMacroCategory())) {
                    %>
                        <li>
                            <a href="${pageContext.request.contextPath}/CatalogServlet?categoryId=<%= category.getId() %>">
                                <%= category.getName() %>
                            </a>
                        </li>
                    <% 
                            }
                        } 
                    %>
                </ul>
            </li>
        <% 
                }
            } else { 
        %>
            <li class="menu-item">
                <a href="#">Catalogo in aggiornamento</a>
            </li>
        <%  } %>
    </ul>
</nav>