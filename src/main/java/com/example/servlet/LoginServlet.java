package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Optional;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        boolean ok = false;

        Optional<User> optUser = new UserDao().findByUsername(login);
        if (optUser.isPresent()) {
            ok = BCrypt.checkpw(password, optUser.get().getPassHash());
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