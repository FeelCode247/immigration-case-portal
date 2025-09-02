-- Adds uploader_role to documents for role-based permissions
ALTER TABLE documents ADD COLUMN uploader_role VARCHAR(20) NOT NULL DEFAULT 'APPLICANT' AFTER uploaded_by;
UPDATE documents d JOIN users u ON u.id=d.uploaded_by SET d.uploader_role=u.role WHERE d.uploader_role IS NOT NULL;
