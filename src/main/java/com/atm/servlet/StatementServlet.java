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

@WebServlet("/statement")
public class StatementServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Integer accId = (Integer) req.getSession().getAttribute("accountId");
        if (accId == null) { res.sendRedirect("login.jsp"); return; }

        List<String[]> rows = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT txn_date, type, amount FROM transactions " +
                 "WHERE account_id = ? ORDER BY txn_date DESC, id DESC LIMIT 10")) {
            ps.setInt(1, accId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rows.add(new String[] {
                    rs.getTimestamp("txn_date").toString(),
                    rs.getString("type"),
                    rs.getBigDecimal("amount").toString()
                });
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.setAttribute("rows", rows);
        req.getRequestDispatcher("statement.jsp").forward(req, res);
    }
}