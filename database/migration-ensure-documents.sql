
-- Migration to ensure documents table has required columns
ALTER TABLE documents
    ADD COLUMN IF NOT EXISTS uploaded_by BIGINT NULL,
    ADD COLUMN IF NOT EXISTS uploader_role VARCHAR(20) NOT NULL DEFAULT 'APPLICANT',
    ADD COLUMN IF NOT EXISTS stored_path VARCHAR(500) NULL,
    ADD COLUMN IF NOT EXISTS content_type VARCHAR(255) NULL,
    ADD COLUMN IF NOT EXISTS uploaded_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP;

-- Legacy columns compatibility
-- If you previously had `file_path`, keep it but also populate stored_path
UPDATE documents SET stored_path = COALESCE(stored_path, file_path);

-- Optional foreign keys
ALTER TABLE documents
    ADD CONSTRAINT IF NOT EXISTS fk_documents_application FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE;

-- No change needed to users table, but DAO now uses full_name instead of username.
