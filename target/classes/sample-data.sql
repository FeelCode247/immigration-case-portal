-- Default admin user (PBKDF2 hash pre-generated for 'admin123')
INSERT INTO users (full_name, email, password_hash, role) VALUES
('System Administrator', 'admin@caseportal.local', 'PBKDF2$65536$YeG9LYEo3FhqvW0Tp/sTqg==$5NL6p8gaGTDQLDe3mMIHhR7xq+oOyD19pQ7QidsZwzs=', 'ADMIN');

-- Sample officer
INSERT INTO users (full_name, email, password_hash, role) VALUES
('Olivia Officer', 'officer@caseportal.local', 'PBKDF2$65536$EolG4W2yL3Yr3iJWoGBt8g==$4msf++dCTBWhP5bkgYeqfp48y+rUSfS6DeW4uUaSj2k=', 'OFFICER');

-- Sample applicant
INSERT INTO users (full_name, email, password_hash, role) VALUES
('Adam Applicant', 'applicant@caseportal.local', 'PBKDF2$65536$FLszTyqJFzTvAi1/lfuBVQ==$ITIJxuFNAZfRbWI1UG15juWwZ753Bs9IpixKPaK8DRk=', 'APPLICANT');

-- Sample application
INSERT INTO applications (applicant_id, title, description, status) VALUES
(3, 'Replace Lost ID', 'Lost my ID card, requesting a replacement.', 'PENDING'),
(3, 'Permit Extension', 'Requesting extension for work permit.', 'IN_REVIEW');
