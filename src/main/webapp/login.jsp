<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Online ATM - Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="center-page">
    <div class="card">
        <h2>Online ATM</h2>
        <% String err = (String) request.getAttribute("error");
           if (err != null) { %><div class="error"><%= err %></div><% } %>
        <form action="login" method="post">
            <input type="text" name="accountNo" placeholder="Account Number" required>
            <input type="password" name="pin" placeholder="PIN" required>
            <button type="submit" class="btn btn-full">Login</button>
        </form>
        <div class="center-link"><a href="index.jsp">Home</a> | <a href="adminlogin.jsp">Admin Login</a></div>
    </div>
</div>
</body>
</html>