<%@ page import="java.io.File" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.file.Files" %>
<%@ page import="java.nio.file.attribute.BasicFileAttributes" %>
<%@ page import="java.nio.file.attribute.FileTime" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Обзор директории</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div style="float:right">
    <a href="${pageContext.request.contextPath}/logout">Выйти</a>
</div>

<h1>Обзор директории</h1>
<p>Время онлайн: <%= request.getAttribute("currentTime") %></p>
<p>Текущий путь: <%= request.getAttribute("currentPath") %></p>

<%
    String parentPath = (String) request.getAttribute("parentPath");
%>

<table>
    <tr><th>Тип</th><th>Имя</th><th class="size">Размер</th><th>Изменён</th></tr>

    <% if (parentPath != null) { %>
    <tr>
        <td colspan="4">
            <a href="${pageContext.request.contextPath}/home?path=<%= URLEncoder.encode(parentPath, "UTF-8") %>">
                &larr; Вверх
            </a>
        </td>
    </tr>
    <% } %>

    <%
        File[] files = (File[]) request.getAttribute("files");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (files != null) {
            for (File f : files) {
                String encoded = URLEncoder.encode(f.getAbsolutePath(), "UTF-8");
                BasicFileAttributes a = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                FileTime ft = a.lastModifiedTime();
    %>
    <tr>
        <td><%= f.isDirectory() ? "Папка" : "Файл" %></td>
        <td>
            <a href="${pageContext.request.contextPath}/home?path=<%= encoded %>">
                <%= f.getName() %><%= f.isDirectory() ? "/" : "" %>
            </a>
        </td>
        <td class="size"><%= f.isDirectory() ? "" : f.length() + " B" %></td>
        <td><%= df.format(new Date(ft.toMillis())) %></td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>
