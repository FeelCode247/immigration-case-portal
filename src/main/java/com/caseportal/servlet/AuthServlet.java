package com.caseportal.servlet;

import com.caseportal.dao.AuditDAO;
import com.caseportal.dao.UserDAO;
import com.caseportal.model.AuditLog;
import com.caseportal.model.User;
import com.caseportal.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("logout".equalsIgnoreCase(action)) {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Logged+out");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("register".equalsIgnoreCase(action)) {
            handleRegister(req, resp);
        } else if ("login".equalsIgnoreCase(action)) {
            handleLogin(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String name = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (name == null || email == null || password == null || name.isBlank() || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.findByEmail(email) != null) {
                req.setAttribute("error", "Email already registered.");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }
            User u = new User();
            u.setFullName(name);
            u.setEmail(email);
            u.setPasswordHash(PasswordUtil.hash(password));
            u.setRole("APPLICANT");
            long id = userDAO.create(u);
            // Auto-login after registration
            u.setId(id);
            req.getSession(true).setAttribute("user", u);
            logAction(u.getId(), req, "REGISTER", "New registration: " + email);
            resp.sendRedirect(req.getContextPath() + "/applicant/dashboard.jsp");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            UserDAO userDAO = new UserDAO();
            User u = userDAO.findByEmail(email);
            if (u == null || !PasswordUtil.verify(password, u.getPasswordHash())) {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
            req.getSession(true).setAttribute("user", u);
            logAction(u.getId(), req, "LOGIN", "User logged in");
            // redirect based on role
            switch (u.getRole()) {
                case "APPLICANT" -> resp.sendRedirect(req.getContextPath() + "/applicant/dashboard.jsp");
                case "OFFICER" -> resp.sendRedirect(req.getContextPath() + "/officer/dashboard.jsp");
                case "ADMIN" -> resp.sendRedirect(req.getContextPath() + "/admin/dashboard.jsp");
                default -> resp.sendRedirect(req.getContextPath() + "/");
            }
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
