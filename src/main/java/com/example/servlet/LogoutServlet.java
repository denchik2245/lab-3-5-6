package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Аннулируем сессию
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Перенаправим на страницу логина
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
