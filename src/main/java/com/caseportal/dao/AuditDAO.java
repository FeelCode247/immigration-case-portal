package com.caseportal.dao;

import com.caseportal.model.AuditLog;
import com.caseportal.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditDAO {

    public long log(AuditLog a) throws SQLException {
        String sql = "INSERT INTO audit_logs(user_id, action, details, ip_address) VALUES (?,?,?,?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (a.getUserId() == null) ps.setNull(1, Types.BIGINT); else ps.setLong(1, a.getUserId());
            ps.setString(2, a.getAction());
            ps.setString(3, a.getDetails());
            ps.setString(4, a.getIpAddress());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public List<AuditLog> listAll() throws SQLException {
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT 500";
        List<AuditLog> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AuditLog a = new AuditLog();
                a.setId(rs.getLong("id"));
                long uid = rs.getLong("user_id");
                a.setUserId(rs.wasNull() ? null : uid);
                a.setAction(rs.getString("action"));
                a.setDetails(rs.getString("details"));
                a.setIpAddress(rs.getString("ip_address"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(a);
            }
        }
        return list;
    }
}
