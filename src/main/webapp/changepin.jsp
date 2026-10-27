<%@ page contentType="text/html; charset=UTF-8" %>
<%
    if (session.getAttribute("accountId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String msg = (String) request.getAttribute("msg");
    String err = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Change PIN</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="header">
    <h1>Online ATM</h1>
    <a href="transaction">Dashboard</a>
</div>
<div class="container">
    <div class="card">
        <h2>Change PIN</h2>
        <% if (msg != null) { %><div class="msg"><%= msg %></div><% } %>
        <% if (err != null) { %><div class="error"><%= err %></div><% } %>
        <form action="changepin" method="post">
            <input type="password" name="oldPin" placeholder="Old PIN" required>
            <input type="password" name="newPin" placeholder="New PIN (4 digit)" maxlength="4" required>
            <input type="password" name="confirmPin" placeholder="Confirm New PIN" maxlength="4" required>
            <button type="submit" class="btn btn-full">Change PIN</button>
        </form>
    </div>
</div>
</body>
</html>