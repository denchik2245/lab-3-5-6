package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Проверка, что пользователь залогинен
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // Нет авторизованного пользователя – отправляем на страницу логина
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Получаем логин пользователя из сессии
        String username = (String) session.getAttribute("user");

        // 2. Определяем "домашнюю" папку пользователя
        // Например: C:\Users\Student\filemanager\<username>
        String userHome = "D:\\Study\\Student\\filemanager\\" + username;

        // Параметр path для навигации внутри домашней папки
        String pathParam = req.getParameter("path");
        String targetPath;

        if (pathParam == null || pathParam.isEmpty()) {
            // Если не передан path, остаёмся в корне домашней директории
            targetPath = userHome;
        } else {
            // Формируем абсолютный путь
            // (конкретная реализация в зависимости от вашей логики)
            targetPath = pathParam;
        }

        File file = new File(targetPath).getCanonicalFile();

        // 3. Не даём смотреть вне домашней папки
        if (!file.getPath().startsWith(new File(userHome).getCanonicalPath())) {
            // Если пользователь пытается залезть вне своей домашней директории
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещён вне домашней папки");
            return;
        }

        if (file.isDirectory()) {
            req.setAttribute("files", file.listFiles());
            req.setAttribute("currentPath", file.getAbsolutePath());
            req.setAttribute("parentPath", file.getParent());
            req.setAttribute("currentTime", new Date());
            req.getRequestDispatcher("/mypage.jsp").forward(req, resp);
        } else if (file.isFile()) {
            Path filePath = Paths.get(file.getAbsolutePath());
            resp.setContentType(Files.probeContentType(filePath));
            resp.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            Files.copy(filePath, resp.getOutputStream());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Если когда-нибудь потребуется POST-обработка, добавляйте логику.
        super.doPost(req, resp);
    }
}
