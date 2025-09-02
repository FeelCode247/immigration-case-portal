package com.caseportal.servlet;

import com.caseportal.dao.AuditDAO;
import com.caseportal.dao.UserDAO;
import com.caseportal.model.AuditLog;
import com.caseportal.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("users".equalsIgnoreCase(action)) {
            try {
                req.setAttribute("users", new UserDAO().listAll());
                req.getRequestDispatcher("/admin/manageUsers.jsp").forward(req, resp);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else if ("audit".equalsIgnoreCase(action)) {
            try {
                req.setAttribute("logs", new AuditDAO().listAll());
                req.getRequestDispatcher("/admin/viewAuditLogs.jsp").forward(req, resp);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("role".equalsIgnoreCase(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            String role = req.getParameter("role");
            try {
                new UserDAO().updateRole(id, role);
                resp.sendRedirect(req.getContextPath() + "/admin?action=users");
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }
}
