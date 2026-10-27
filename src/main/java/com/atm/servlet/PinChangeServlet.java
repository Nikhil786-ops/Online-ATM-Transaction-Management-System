package com.atm.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.atm.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/changepin")
public class PinChangeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Integer accId = (Integer) req.getSession().getAttribute("accountId");
        if (accId == null) { res.sendRedirect("login.jsp"); return; }

        String oldPin = req.getParameter("oldPin");
        String newPin = req.getParameter("newPin");
        String confirmPin = req.getParameter("confirmPin");

        if (!newPin.matches("\\d{4}")) {
            req.setAttribute("error", "New PIN sirf 4 digit ka hona chahiye");
        } else if (!newPin.equals(confirmPin)) {
            req.setAttribute("error", "New PIN aur Confirm PIN match nahi kar rahe");
        } else {
            try (Connection con = DBConnection.getConnection()) {
                boolean oldOk;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT 1 FROM accounts WHERE id = ? AND pin = ?")) {
                    ps.setInt(1, accId);
                    ps.setString(2, oldPin);
                    ResultSet rs = ps.executeQuery();
                    oldOk = rs.next();
                }
                if (!oldOk) {
                    req.setAttribute("error", "Old PIN galat hai");
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE accounts SET pin = ? WHERE id = ?")) {
                        ps.setString(1, newPin);
                        ps.setInt(2, accId);
                        ps.executeUpdate();
                    }
                    req.setAttribute("msg", "PIN successfully change ho gaya");
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        req.getRequestDispatcher("changepin.jsp").forward(req, res);
    }
}