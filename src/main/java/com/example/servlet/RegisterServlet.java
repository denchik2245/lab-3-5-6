package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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

        UserDao dao = new UserDao();
        Optional<User> existing = dao.findByUsername(login);
        if (existing.isPresent()) {
            req.setAttribute("registerFailed", "Логин уже занят");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        Files.createDirectories(userDir);
        User user = new User(login, hash, email, userDir.toString());
        dao.save(user);

        req.getSession().setAttribute("user", login);
        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }
}