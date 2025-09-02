package com.caseportal.servlet;

import com.caseportal.dao.ApplicationDAO;
import com.caseportal.dao.AuditDAO;
import com.caseportal.dao.DocumentDAO;
import com.caseportal.model.Application;
import com.caseportal.model.AuditLog;
import com.caseportal.model.Document;
import com.caseportal.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ApplicationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("list".equalsIgnoreCase(action)) {
            handleList(req, resp);
        } else if ("pending".equalsIgnoreCase(action)) {
            handlePending(req, resp);
        } else if ("detail".equalsIgnoreCase(action)) {
            handleDetail(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("create".equalsIgnoreCase(action)) {
            handleCreate(req, resp);
        } else if ("updateStatus".equalsIgnoreCase(action)) {
            handleUpdateStatus(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        try {
            Application a = new Application();
            a.setApplicantId(user.getId());
            a.setTitle(title);
            a.setDescription(description);
            a.setStatus("PENDING");
            long id = new ApplicationDAO().create(a);
            logAction(user.getId(), req, "SUBMIT_APP", "Application ID " + id + " created");
            resp.sendRedirect(req.getContextPath() + "/applications?action=list");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            List<Application> apps = new ApplicationDAO().findByApplicant(user.getId());
            req.setAttribute("applications", apps);
            req.getRequestDispatcher("/applicant/viewStatus.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handlePending(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Application> apps = new ApplicationDAO().listPending();
            req.setAttribute("applications", apps);
            req.getRequestDispatcher("/officer/reviewApplications.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handleDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long appId = Long.parseLong(req.getParameter("id"));

            // Fetch application details
            Application app = new ApplicationDAO().findById(appId);

            // Fetch documents for this application
            List<Document> docs = new DocumentDAO().findByApplicationId(appId);

            // Set attributes for JSP
            req.setAttribute("application", app);
            req.setAttribute("documents", docs);

            // Forward to JSP
            req.getRequestDispatcher("/applicationDetails.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handleUpdateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");
        long appId = Long.parseLong(req.getParameter("id"));
        String status = req.getParameter("status");
        try {
            new ApplicationDAO().updateStatus(appId, status);
            logAction(user.getId(), req, "UPDATE_STATUS", "Application ID " + appId + " -> " + status);
            resp.sendRedirect(req.getContextPath() + "/applications?action=pending");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void logAction(Long userId, HttpServletRequest req, String action, String details) {
        try {
            AuditLog a = new AuditLog();
            a.setUserId(userId);
            a.setAction(action);
            a.setDetails(details);
            a.setIpAddress(req.getRemoteAddr());
            new AuditDAO().log(a);
        } catch (Exception ignored) {}
    }
}
