package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class LoginServlet extends HttpServlet {

    private static final Path USERS_ROOT = Paths.get("D:/Study/Student/filemanager");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login    = req.getParameter("login");
        String password = req.getParameter("password");

        Path propsFile = USERS_ROOT.resolve(login).resolve("user.properties");

        boolean ok = false;
        if (Files.exists(propsFile)) {
            Properties p = new Properties();
            try (InputStream in = Files.newInputStream(propsFile)) {
                p.load(in);
            }
            String savedPass = p.getProperty("password");
            ok = password != null && password.equals(savedPass);
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