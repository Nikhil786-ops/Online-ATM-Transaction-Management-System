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
    <title>ATM Dashboard</title>
    <style>
        body { font-family: Arial; background: #e8f0fe; margin: 0; padding: 30px; }
        .box { background: white; max-width: 450px; margin: auto; padding: 25px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.2); }
        .bal { font-size: 22px; color: #1a73e8; }
        input { width: 100%; padding: 8px; margin: 6px 0; box-sizing: border-box; }
        button { padding: 10px 16px; background: #1a73e8; color: white; border: none; cursor: pointer; margin-right: 6px; }
        .msg { color: green; } .error { color: red; }
        a { color: #1a73e8; }
    </style>
</head>
<body>
<div class="box">
    <h2>Welcome, <%= session.getAttribute("name") %></h2>
    <p class="bal">Balance: Rs. <%= request.getAttribute("balance") != null ? request.getAttribute("balance") : "Click Refresh" %></p>
    <% if (msg != null) { %><p class="msg"><%= msg %></p><% } %>
    <% if (err != null) { %><p class="error"><%= err %></p><% } %>

    <form action="transaction" method="post">
        <input type="number" name="amount" step="0.01" min="1" placeholder="Amount" required>
        <button type="submit" name="action" value="deposit">Deposit</button>
        <button type="submit" name="action" value="withdraw">Withdraw</button>
    </form>
    <br>
    <a href="transaction">Refresh Balance</a> |
    <a href="statement">Mini Statement</a> |
    <a href="changepin.jsp">Change PIN</a> |
    <a href="logout">Logout</a>
</div>
</body>
</html>