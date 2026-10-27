<%@ page contentType="text/html; charset=UTF-8" import="java.util.List" %>
<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("adminlogin.jsp");
        return;
    }
    List<String[]> rows = (List<String[]>) request.getAttribute("rows");
    String msg = (String) request.getAttribute("msg");
    String err = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Panel</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="header">
    <h1>Admin Panel</h1>
    <a href="adminlogout">Logout</a>
</div>
<div class="container">
    <div class="card">
        <% if (msg != null) { %><div class="msg"><%= msg %></div><% } %>
        <% if (err != null) { %><div class="error"><%= err %></div><% } %>
        <h3>Naya Account Banao</h3>
        <form action="admin" method="post" class="inline-form">
            <input type="hidden" name="action" value="create">
            <input type="text" name="accountNo" placeholder="Account No" required>
            <input type="text" name="name" placeholder="Name" required>
            <input type="password" name="pin" placeholder="PIN (4 digit)" maxlength="4" required>
            <input type="number" name="balance" step="0.01" min="0" placeholder="Opening Balance" required>
            <button type="submit" class="btn">Create</button>
        </form>
    </div>

    <div class="card">
        <h3>Saare Accounts</h3>
        <div class="table-wrap">
        <table>
            <tr><th>ID</th><th>Account No</th><th>Name</th><th>Balance</th><th>Action</th></tr>
            <% if (rows != null) { for (String[] r : rows) { %>
            <tr>
                <td><%= r[0] %></td><td><%= r[1] %></td><td><%= r[2] %></td><td><%= r[3] %></td>
                <td>
                    <form action="admin" method="post" onsubmit="return confirm('Account delete karna hai?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="<%= r[0] %>">
                        <button type="submit" class="btn btn-red btn-sm">Delete</button>
                    </form>
                </td>
            </tr>
            <% } } %>
        </table>
        </div>
    </div>
</div>
</body>
</html>