package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;

public class RegisterServlet extends HttpServlet {
    private static final Path USERS_ROOT = Paths.get("D:/Study/Student/filemanager");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login    = req.getParameter("login");
        String password = req.getParameter("password");
        String email    = req.getParameter("email");

        Path userDir   = USERS_ROOT.resolve(login);
        Path propsFile = userDir.resolve("user.properties");

        if (Files.exists(userDir)) {
            req.setAttribute("registerFailed", "Логин уже занят");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        Files.createDirectories(userDir);
        try (BufferedWriter bw = Files.newBufferedWriter(propsFile)) {
            bw.write("password=" + password + System.lineSeparator()); // DEMO! храните хэш
            bw.write("email="    + email    + System.lineSeparator());
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
