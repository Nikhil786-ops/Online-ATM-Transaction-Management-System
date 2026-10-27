<%@ page contentType="text/html; charset=UTF-8" import="java.util.List" %>
<%
    if (session.getAttribute("accountId") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<String[]> rows = (List<String[]>) request.getAttribute("rows");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Mini Statement</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="header">
    <h1>Online ATM</h1>
    <a href="transaction">Dashboard</a>
</div>
<div class="container">
    <div class="card">
        <h2>Mini Statement (Last 10)</h2>
        <div class="table-wrap">
        <table>
            <tr><th>Date</th><th>Type</th><th>Amount (Rs.)</th></tr>
            <% if (rows == null || rows.isEmpty()) { %>
                <tr><td colspan="3">Koi transaction nahi mili</td></tr>
            <% } else { for (String[] r : rows) { %>
                <tr><td><%= r[0] %></td><td class="<%= r[1] %>"><%= r[1] %></td><td><%= r[2] %></td></tr>
            <% } } %>
        </table>
        </div>
    </div>
</div>
</body>
</html>