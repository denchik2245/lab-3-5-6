package com.example.servlet;

import com.example.servlet.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login    = req.getParameter("login");
        String password = req.getParameter("password");

        boolean ok = false;
        String query = "SELECT password FROM users WHERE login = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(query)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String savedPass = rs.getString("password");
                    ok = password.equals(savedPass); // либо проверка хэша
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (ok) {
            req.getSession().setAttribute("user", login);
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
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