<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Online ATM Transaction Management System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <style>
        .home-title { text-align: center; color: #fff; margin-bottom: 25px; }
        .home-title h1 { margin: 0 0 6px 0; font-size: 28px; }
        .home-title p { margin: 0; opacity: .85; }
        .options { display: flex; gap: 20px; flex-wrap: wrap; justify-content: center; }
        .option { background: #fff; border-radius: 12px; padding: 28px; width: 250px; text-align: center; box-shadow: 0 4px 14px rgba(0,0,0,.2); }
        .option h3 { margin-bottom: 8px; }
        .option p { color: #555; font-size: 14px; margin-bottom: 18px; }
    </style>
</head>
<body>
<div class="center-page" style="flex-direction: column;">
    <div class="home-title">
        <h1>Online ATM Transaction Management System</h1>
        <p>Secure banking using Servlet, JSP and JDBC</p>
    </div>
    <div class="options">
        <div class="option">
            <h3>User Login</h3>
            <p>Balance, deposit, withdraw, mini statement aur PIN change</p>
            <a href="login.jsp" class="btn btn-full">Login as User</a>
        </div>
        <div class="option">
            <h3>Admin Login</h3>
            <p>Accounts create, view aur delete karo</p>
            <a href="adminlogin.jsp" class="btn btn-full btn-red">Login as Admin</a>
        </div>
    </div>
</div>
</body>
</html>