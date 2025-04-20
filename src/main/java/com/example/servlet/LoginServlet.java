package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        boolean ok = false;

        try (Connection c = Db.get();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT pass_hash FROM users WHERE username = ?")) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("pass_hash");
                    ok = BCrypt.checkpw(password, hash);
                }
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (ok) {
            req.getSession().setAttribute("user", login);
            resp.sendRedirect(req.getContextPath() + "/home");
        }

        else {
            req.setAttribute("loginFailed", true);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
