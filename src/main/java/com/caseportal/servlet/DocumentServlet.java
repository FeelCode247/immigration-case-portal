package com.caseportal.servlet;

import com.caseportal.dao.AuditDAO;
import com.caseportal.dao.DocumentDAO;
import com.caseportal.model.AuditLog;
import com.caseportal.model.Document;
import com.caseportal.model.User;
import com.caseportal.util.FileUploadUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@MultipartConfig
public class DocumentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = Optional.ofNullable(req.getParameter("action")).orElse("").toLowerCase();
        switch (action) {
            case "upload":
                handleUpload(req, resp);
                break;
            case "delete":
                handleDelete(req, resp);
                break;
            case "replace":
            case "update": // Java 8/11 compatible
                handleReplace(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = Optional.ofNullable(req.getParameter("action")).orElse("").toLowerCase();
        switch (action) {
            case "download":
                handleDownload(req, resp);
                break;
            case "list":
                handleList(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    /** ---------------- Upload ---------------- */
    private void handleUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = getAuthenticatedUser(req, resp);
        if (user == null) return;

        String appParam = req.getParameter("applicationId");
        if (appParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing applicationId");
            return;
        }

        long appId = Long.parseLong(appParam);
        Part part = req.getPart("file");

        if (part == null || part.getSize() == 0) {
            redirectWithMsg(req, resp, appId, "error=Please+select+a+file");
            return;
        }

        String fileName = part.getSubmittedFileName();
        String contentType = part.getContentType();
        File baseDir = FileUploadUtil.getOrCreateUploadDir(getServletContext());
        File appDir = new File(baseDir, "app_" + appId);
        if (!appDir.exists() && !appDir.mkdirs()) {
            throw new ServletException("Failed to create upload directory");
        }

        File dest = FileUploadUtil.uniquify(new File(appDir, fileName));

        try (InputStream in = part.getInputStream();
             OutputStream out = new FileOutputStream(dest)) {
            in.transferTo(out);
        }

        try {
            Document d = new Document();
            d.setApplicationId(appId);
            d.setUploadedBy(user.getId());
            d.setUploaderRole(user.getRole());
            d.setFileName(fileName);
            d.setStoredPath(dest.getAbsolutePath());
            d.setContentType(contentType);
            d.setUploadedAt(new Timestamp(System.currentTimeMillis())); // ✅ added

            new DocumentDAO().add(d);
            logAction(user.getId(), req, "UPLOAD_DOC", "Uploaded: " + fileName);

            redirectWithMsg(req, resp, appId, "msg=uploaded");
        } catch (Exception e) {
            throw new ServletException("Upload failed", e);
        }
    }

    /** ---------------- Download ---------------- */
    private void handleDownload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing document ID");
            return;
        }

        long id = Long.parseLong(idParam);
        try {
            Document d = new DocumentDAO().findById(id);
            if (d == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Document not found");
                return;
            }
            File f = new File(d.getStoredPath());
            if (!f.exists()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File missing on server");
                return;
            }

            String safeFileName = d.getFileName().replaceAll("[\\r\\n\"]", "_"); //  sanitization
            resp.setContentType(d.getContentType() != null ? d.getContentType() : "application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + safeFileName + "\"");

            try (InputStream in = new FileInputStream(f); OutputStream out = resp.getOutputStream()) {
                in.transferTo(out);
            }
        } catch (Exception e) {
            throw new ServletException("Download failed", e);
        }
    }

    /** ---------------- List ---------------- */
    private void handleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String appParam = req.getParameter("applicationId");
        if (appParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing applicationId");
            return;
        }

        long appId = Long.parseLong(appParam);
        try {
            var docs = new DocumentDAO().listByApplication(appId);
            req.setAttribute("documents", docs);
            req.getRequestDispatcher("/applicationDetails.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Failed to list documents", e);
        }
    }

    /** ---------------- Delete ---------------- */
    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = getAuthenticatedUser(req, resp);
        if (user == null) return;

        long id = Long.parseLong(req.getParameter("id"));
        long appId = Long.parseLong(req.getParameter("applicationId"));

        try {
            DocumentDAO dao = new DocumentDAO();
            Document d = dao.findById(id);
            if (d == null) {
                redirectWithMsg(req, resp, appId, "error=Document+not+found");
                return;
            }

            ensureCanModify(user, d);
            Files.deleteIfExists(java.nio.file.Path.of(d.getStoredPath()));
            dao.deleteById(id);

            logAction(user.getId(), req, "DELETE_DOC", "Deleted: " + d.getFileName());
            redirectWithMsg(req, resp, appId, "msg=deleted");
        } catch (Exception e) {
            throw new ServletException("Delete failed", e);
        }
    }

    /** ---------------- Replace ---------------- */
    private void handleReplace(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = getAuthenticatedUser(req, resp);
        if (user == null) return;

        long id = Long.parseLong(req.getParameter("id"));
        long appId = Long.parseLong(req.getParameter("applicationId"));

        try {
            DocumentDAO dao = new DocumentDAO();
            Document existing = dao.findById(id);
            if (existing == null) {
                redirectWithMsg(req, resp, appId, "error=Document+not+found");
                return;
            }

            ensureCanModify(user, existing);

            Part part = req.getPart("file");
            if (part == null || part.getSize() == 0) {
                redirectWithMsg(req, resp, appId, "error=Please+select+a+file");
                return;
            }

            String original = part.getSubmittedFileName();
            String ct = part.getContentType();

            File base = FileUploadUtil.getOrCreateUploadDir(getServletContext());
            File appDir = new File(base, "app_" + appId);
            if (!appDir.exists() && !appDir.mkdirs()) {
                throw new ServletException("Failed to create upload directory");
            }

            File dest = FileUploadUtil.uniquify(new File(appDir, original));

            try (InputStream in = part.getInputStream(); OutputStream out = new FileOutputStream(dest)) {
                in.transferTo(out);
            }

            // delete old file (best-effort)
            try { Files.deleteIfExists(java.nio.file.Path.of(existing.getStoredPath())); } catch (Exception ignore) {}

            existing.setFileName(original);
            existing.setStoredPath(dest.getAbsolutePath());
            existing.setContentType(ct);
            existing.setUploadedAt(new Timestamp(System.currentTimeMillis())); // ✅ update timestamp

            dao.replace(existing); // ⚠️ ensure DocumentDAO has replace(); change to update() if needed

            logAction(user.getId(), req, "REPLACE_DOC", "Replaced: " + original);
            redirectWithMsg(req, resp, appId, "msg=replaced");
        } catch (Exception e) {
            throw new ServletException("Replace failed", e);
        }
    }

    /** ---------------- Helpers ---------------- */
    private void ensureCanModify(User user, Document d) throws ServletException {
        if (user == null) throw new ServletException("Not authenticated");
        String role = user.getRole();
        if (d.getUploaderRole() != null && !role.equalsIgnoreCase(d.getUploaderRole())) {
            throw new ServletException("Forbidden: cannot modify documents uploaded by another role");
        }
    }

    private User getAuthenticatedUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?error=Please+login");
            return null; // stop further execution
        }
        return user;
    }

    private void redirectWithMsg(HttpServletRequest req, HttpServletResponse resp, long appId, String msg) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/applications?action=detail&id=" + appId + "&" + msg);
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
