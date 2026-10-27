package com.atm.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.atm.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/adminlogin")
public class AdminLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String user = req.getParameter("username");
        String pass = req.getParameter("password");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT id FROM admins WHERE username = ? AND password = ?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                req.getSession().setAttribute("adminId", rs.getInt("id"));
                res.sendRedirect("admin");
            } else {
                req.setAttribute("error", "Invalid admin username or password");
                req.getRequestDispatcher("adminlogin.jsp").forward(req, res);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}