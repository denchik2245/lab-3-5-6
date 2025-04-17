<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Вход</title>
</head>
<body>
<h1>Вход</h1>
<form method="post" action="${pageContext.request.contextPath}/login">
    <p>Логин: <input type="text" name="login"/></p>
    <p>Пароль: <input type="password" name="password"/></p>
    <button type="submit">Войти</button>
</form>

<p>Еще не зарегистрированы? <a href="${pageContext.request.contextPath}/register">Регистрация</a></p>

<%
    Boolean loginFailed = (Boolean) request.getAttribute("loginFailed");
    if (loginFailed != null && loginFailed) {
%>
<p style="color:red;">Неверный логин или пароль</p>
<%
    }
%>
</body>
</html>

