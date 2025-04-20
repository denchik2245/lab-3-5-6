package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {
    private static final Path USERS_ROOT = Paths.get("D:/Study/Student/filemanager");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        if (login == null || password == null || email == null ||
                login.isBlank() || password.isBlank() || email.isBlank()) {
            req.setAttribute("registerFailed", "Все поля обязательны");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        Path userDir = USERS_ROOT.resolve(login);

        try (Connection c = Db.get()) {

            try (PreparedStatement chk = c.prepareStatement(
                    "SELECT 1 FROM users WHERE username = ?")) {
                chk.setString(1, login);
                if (chk.executeQuery().next()) {
                    req.setAttribute("registerFailed", "Логин уже занят");
                    req.getRequestDispatcher("/register.jsp").forward(req, resp);
                    return;
                }
            }

            Files.createDirectories(userDir);

            try (PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO users(username, pass_hash, email, home_dir) " +
                            "VALUES (?,?,?,?)")) {
                ins.setString(1, login);
                ins.setString(2, hash);
                ins.setString(3, email);
                ins.setString(4, userDir.toString());
                ins.executeUpdate();
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        req.getSession().setAttribute("user", login);
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }
}
