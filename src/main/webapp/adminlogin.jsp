<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Login</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="center-page">
    <div class="card">
        <h2>Admin Login</h2>
        <% String err = (String) request.getAttribute("error");
           if (err != null) { %><div class="error"><%= err %></div><% } %>
        <form action="adminlogin" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit" class="btn btn-full">Login</button>
        </form>
        <div class="center-link"><a href="login.jsp">User Login</a></div>
    </div>
</div>
</body>
</html>