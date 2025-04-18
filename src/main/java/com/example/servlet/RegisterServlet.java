package com.example.servlet;

import com.example.servlet.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    private static final Path USERS_ROOT = Paths.get("D:/Study/Student/filemanager");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login    = req.getParameter("login");
        String password = req.getParameter("password");
        String email    = req.getParameter("email");

        // 1) Сохранить в БД
        String insertSQL = "INSERT INTO users(login,password,email) VALUES (?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(insertSQL)) {
            ps.setString(1, login);
            ps.setString(2, password); // рекомендую хэшировать!
            ps.setString(3, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            // если duplicate key
            req.setAttribute("registerFailed", "Логин уже занят");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 2) Создать домашнюю папку (как и раньше)
        Path userDir = USERS_ROOT.resolve(login);
        Files.createDirectories(userDir);

        // 3) Установить сессию и редирект
        req.getSession().setAttribute("user", login);
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }
}