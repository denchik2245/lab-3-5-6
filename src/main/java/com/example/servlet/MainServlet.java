package com.example.servlet;

import jakarta.servlet.ServletException;
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
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String username = (String) session.getAttribute("user");
        String userHome = "D:\\Study\\Student\\filemanager\\" + username;
        String pathParam = req.getParameter("path");
        String targetPath;

        if (pathParam == null || pathParam.isEmpty()) {
            targetPath = userHome;
        } else {
            targetPath = pathParam;
        }

        File file = new File(targetPath).getCanonicalFile();

        if (!file.getPath().startsWith(new File(userHome).getCanonicalPath())) {
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
        super.doPost(req, resp);
    }
}
