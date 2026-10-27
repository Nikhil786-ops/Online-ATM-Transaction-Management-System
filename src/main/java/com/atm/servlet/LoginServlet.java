package com.atm.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.atm.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String accountNo = req.getParameter("accountNo");
        String pin = req.getParameter("pin");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT id, name FROM accounts WHERE account_no = ? AND pin = ?")) {
            ps.setString(1, accountNo);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("accountId", rs.getInt("id"));
                session.setAttribute("name", rs.getString("name"));
                res.sendRedirect("transaction");
            } else {
                req.setAttribute("error", "Invalid account number or PIN");
                req.getRequestDispatcher("login.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}