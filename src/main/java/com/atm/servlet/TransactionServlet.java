package com.atm.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.atm.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {

    // GET: sirf balance dikhane ke liye
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Integer accId = (Integer) req.getSession().getAttribute("accountId");
        if (accId == null) { res.sendRedirect("login.jsp"); return; }

        try (Connection con = DBConnection.getConnection()) {
            req.setAttribute("balance", getBalance(con, accId));
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("dashboard.jsp").forward(req, res);
    }

    // POST: deposit ya withdraw
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Integer accId = (Integer) req.getSession().getAttribute("accountId");
        if (accId == null) { res.sendRedirect("login.jsp"); return; }

        String action = req.getParameter("action");
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // transaction shuru
            BigDecimal balance = getBalance(con, accId);

            if (amount.signum() <= 0) {
                req.setAttribute("error", "Amount 0 se zyada hona chahiye");
            } else if (action.equals("withdraw") && amount.compareTo(balance) > 0) {
                req.setAttribute("error", "Insufficient balance");
            } else {
                String sql = action.equals("deposit")
                    ? "UPDATE accounts SET balance = balance + ? WHERE id = ?"
                    : "UPDATE accounts SET balance = balance - ? WHERE id = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setBigDecimal(1, amount);
                    ps.setInt(2, accId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO transactions (account_id, type, amount) VALUES (?, ?, ?)")) {
                    ps.setInt(1, accId);
                    ps.setString(2, action);
                    ps.setBigDecimal(3, amount);
                    ps.executeUpdate();
                }
                con.commit(); // dono kaam saath mein save
                req.setAttribute("msg", action + " successful");
            }
            req.setAttribute("balance", getBalance(con, accId));
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("dashboard.jsp").forward(req, res);
    }

    private BigDecimal getBalance(Connection con, int accId) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT balance FROM accounts WHERE id = ?")) {
            ps.setInt(1, accId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getBigDecimal("balance");
        }
    }
}