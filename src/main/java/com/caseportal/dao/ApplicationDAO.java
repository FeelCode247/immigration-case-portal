package com.caseportal.dao;

import com.caseportal.model.Application;
import com.caseportal.model.Document;
import com.caseportal.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    // Create new application
    public long create(Application a) throws SQLException {
        String sql = "INSERT INTO applications(applicant_id, title, description, status, submitted_date) VALUES (?,?,?,?,NOW())";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, a.getApplicantId());
            ps.setString(2, a.getTitle());
            ps.setString(3, a.getDescription());
            ps.setString(4, a.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    // Get applications by status
    public List<Application> getApplicationsByStatus(String status) {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name AS applicant_name FROM applications a " +
                     "JOIN users u ON a.applicant_id = u.id WHERE a.status=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application app = map(rs);

                    // Attach documents
                    List<Document> docs = new DocumentDAO().findByApplicationId(app.getId());
                    app.setDocuments(docs);

                    apps.add(app);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apps;
    }

    // Find applications by applicant (with documents)
    public List<Application> findByApplicant(long applicantId) throws SQLException {
        String sql = "SELECT a.*, u.full_name AS applicant_name FROM applications a " +
                     "JOIN users u ON a.applicant_id=u.id WHERE applicant_id=? ORDER BY created_at DESC";
        List<Application> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, applicantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application app = map(rs);

                    // Attach documents
                    List<Document> docs = new DocumentDAO().findByApplicationId(app.getId());
                    app.setDocuments(docs);

                    list.add(app);
                }
            }
        }
        return list;
    }

    // Find application by ID (with documents)
    public Application findById(long id) throws SQLException {
        String sql = "SELECT a.*, u.full_name AS applicant_name FROM applications a " +
                     "JOIN users u ON a.applicant_id=u.id WHERE a.id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Application app = map(rs);

                    // Attach documents
                    List<Document> docs = new DocumentDAO().findByApplicationId(app.getId());
                    app.setDocuments(docs);

                    return app;
                }
            }
        }
        return null;
    }

    // Update status only
    public void updateStatus(long id, String status) throws SQLException {
        String sql = "UPDATE applications SET status=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    // Full update
    public void update(Application a) throws SQLException {
        String sql = "UPDATE applications SET title=?, description=?, status=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getDescription());
            ps.setString(3, a.getStatus());
            ps.setLong(4, a.getId());
            ps.executeUpdate();
        }
    }

    // Delete
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM applications WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // Find all applications (with documents)
    public List<Application> findAll() throws SQLException {
        String sql = "SELECT a.*, u.full_name AS applicant_name FROM applications a " +
                     "JOIN users u ON a.applicant_id=u.id ORDER BY created_at DESC";
        List<Application> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Application app = map(rs);

                // Attach documents
                List<Document> docs = new DocumentDAO().findByApplicationId(app.getId());
                app.setDocuments(docs);

                list.add(app);
            }
        }
        return list;
    }

    // List all applications that are pending or in review (with documents)
    public List<Application> listPending() throws SQLException {
        String sql = "SELECT * FROM applications WHERE status IN ('PENDING','IN_REVIEW') ORDER BY created_at ASC";
        List<Application> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Application app = map(rs);

                // attach documents
                List<Document> docs = new DocumentDAO().findByApplicationId(app.getId());
                app.setDocuments(docs);

                list.add(app);
            }
        }
        return list;
    }

    // Map DB row → Application
    private Application map(ResultSet rs) throws SQLException {
        Application a = new Application();
        a.setId(rs.getLong("id"));
        a.setApplicantId(rs.getLong("applicant_id"));
        a.setTitle(rs.getString("title"));
        a.setDescription(rs.getString("description"));
        a.setStatus(rs.getString("status"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        a.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Optional fields
        try {
            a.setApplicantName(rs.getString("applicant_name"));
        } catch (SQLException ignored) {}
        try {
            a.setSubmittedDate(rs.getTimestamp("submitted_date"));
        } catch (SQLException ignored) {}

        return a;
    }
}
