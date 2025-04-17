<%--
  Created by IntelliJ IDEA.
  User: denis
  Date: 16.04.2025
  Time: 18:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Регистрация</title>
</head>
<body>
<h1>Регистрация</h1>
<form method="post" action="${pageContext.request.contextPath}/register">
    <p>Логин: <input type="text" name="login"/></p>
    <p>Пароль: <input type="password" name="password"/></p>
    <p>Email: <input type="email" name="email"/></p>
    <button type="submit">Зарегистрироваться</button>
</form>

<p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войти</a></p>
</body>
</html>
