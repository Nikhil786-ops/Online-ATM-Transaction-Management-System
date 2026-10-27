package com.atm.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.atm.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private boolean isAdmin(HttpServletRequest req) {
        return req.getSession().getAttribute("adminId") != null;
    }

    // READ: saare accounts dikhao
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isAdmin(req)) { res.sendRedirect("adminlogin.jsp"); return; }

        List<String[]> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT id, account_no, name, balance FROM accounts ORDER BY id")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rows.add(new String[] {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("account_no"),
                    rs.getString("name"),
                    rs.getBigDecimal("balance").toString()
                });
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.setAttribute("rows", rows);
        req.getRequestDispatcher("admin.jsp").forward(req, res);
    }

    // CREATE aur DELETE
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (!isAdmin(req)) { res.sendRedirect("adminlogin.jsp"); return; }

        String action = req.getParameter("action");
        try (Connection con = DBConnection.getConnection()) {
            if ("create".equals(action)) {
                String accNo = req.getParameter("accountNo");
                String name = req.getParameter("name");
                String pin = req.getParameter("pin");
                String bal = req.getParameter("balance");
                if (!pin.matches("\\d{4}")) {
                    req.setAttribute("error", "PIN 4 digit ka hona chahiye");
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO accounts (account_no, name, pin, balance) VALUES (?, ?, ?, ?)")) {
                        ps.setString(1, accNo);
                        ps.setString(2, name);
                        ps.setString(3, pin);
                        ps.setBigDecimal(4, new java.math.BigDecimal(bal));
                        ps.executeUpdate();
                        req.setAttribute("msg", "Account create ho gaya");
                    } catch (java.sql.SQLException e) {
                        req.setAttribute("error", "Account number pehle se exist karta hai");
                    }
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                con.setAutoCommit(false);
                try (PreparedStatement ps1 = con.prepareStatement(
                         "DELETE FROM transactions WHERE account_id = ?");
                     PreparedStatement ps2 = con.prepareStatement(
                         "DELETE FROM accounts WHERE id = ?")) {
                    ps1.setInt(1, id);
                    ps1.executeUpdate();
                    ps2.setInt(1, id);
                    ps2.executeUpdate();
                    con.commit();
                    req.setAttribute("msg", "Account delete ho gaya");
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        doGet(req, res);
    }
}