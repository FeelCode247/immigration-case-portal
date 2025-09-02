package com.caseportal.model;

import java.sql.Timestamp;

public class Document {
    private Long id;
    private Long applicationId;
    private Long uploadedBy;
    private String uploaderRole;
    private String fileName;
    private String storedPath;
    private String contentType;
    private Timestamp uploadedAt;

    public Document() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getStoredPath() { return storedPath; }
    public void setStoredPath(String storedPath) { this.storedPath = storedPath; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Timestamp getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }

    public String getUploaderRole() { return uploaderRole; }
    public void setUploaderRole(String uploaderRole) { this.uploaderRole = uploaderRole; }

    // 🚀 Alias methods so JSP can use ${d.role}
    public String getRole() { return uploaderRole; }
    public void setRole(String role) { this.uploaderRole = role; }
}
