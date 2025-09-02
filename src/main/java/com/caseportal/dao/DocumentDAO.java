package com.caseportal.dao;

import com.caseportal.model.Document;
import com.caseportal.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public long add(Document d) throws SQLException {
        String sql = "INSERT INTO documents (application_id, uploaded_by, uploader_role, file_name, stored_path, content_type, uploaded_at) " +
                     "VALUES (?,?,?,?,?,?,CURRENT_TIMESTAMP)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, d.getApplicationId());
            if (d.getUploadedBy() == null) ps.setNull(2, Types.BIGINT); else ps.setLong(2, d.getUploadedBy());
            ps.setString(3, d.getUploaderRole());
            ps.setString(4, d.getFileName());
            ps.setString(5, d.getStoredPath());
            ps.setString(6, d.getContentType());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            return 0L;
        }
    }

    public Document findById(long id) throws SQLException {
        String sql = "SELECT * FROM documents WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }

    public List<Document> listByApplication(long applicationId) throws SQLException {
        String sql = "SELECT * FROM documents WHERE application_id=? ORDER BY uploaded_at DESC";
        List<Document> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public int deleteById(long id) throws SQLException {
        String sql = "DELETE FROM documents WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }

    public int replace(Document d) throws SQLException {
        String sql = "UPDATE documents SET file_name=?, stored_path=?, content_type=?, uploaded_at=CURRENT_TIMESTAMP WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getFileName());
            ps.setString(2, d.getStoredPath());
            ps.setString(3, d.getContentType());
            ps.setLong(4, d.getId());
            return ps.executeUpdate();
        }
    }

    public List<Document> findByApplicationId(long applicationId) throws SQLException {
        return listByApplication(applicationId);
    }

    private Document map(ResultSet rs) throws SQLException {
        Document d = new Document();
        d.setId(rs.getLong("id"));
        d.setApplicationId(rs.getLong("application_id"));
        long up = rs.getLong("uploaded_by");
        if (!rs.wasNull()) d.setUploadedBy(up); else d.setUploadedBy(null);

        try {
            d.setUploaderRole(rs.getString("uploader_role"));
        } catch (SQLException ignored) {
            try { d.setUploaderRole(rs.getString("role")); } catch (SQLException e) { d.setUploaderRole(null); }
        }

        try {
            d.setStoredPath(rs.getString("stored_path"));
        } catch (SQLException ignored) {
            try { d.setStoredPath(rs.getString("file_path")); } catch (SQLException e) { d.setStoredPath(null); }
        }

        d.setFileName(rs.getString("file_name"));

        try {
            d.setContentType(rs.getString("content_type"));
        } catch (SQLException ignored) {
            try { d.setContentType(rs.getString("file_type")); } catch (SQLException e) { d.setContentType(null); }
        }

        // FIX: store as Timestamp directly (not Instant)
        try {
            Timestamp ts = rs.getTimestamp("uploaded_at");
            d.setUploadedAt(ts);
        } catch (SQLException ignored) {
            try {
                Timestamp ts = rs.getTimestamp("upload_date");
                d.setUploadedAt(ts);
            } catch (SQLException e) {
                d.setUploadedAt(null);
            }
        }
        return d;
    }
}
