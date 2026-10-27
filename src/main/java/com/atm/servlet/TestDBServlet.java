package com.atm.servlet;

import java.io.IOException;
import java.sql.Connection;
import com.atm.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/testdb")
public class TestDBServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        try (Connection con = DBConnection.getConnection()) {
            res.getWriter().println("<h1>DB connected!</h1>");
        } catch (Exception e) {
            res.getWriter().println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }
}