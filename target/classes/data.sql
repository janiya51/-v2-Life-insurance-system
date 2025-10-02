-- ======================================================
-- SAMPLE DATA INSERTS (Using snake_case table names for H2)
-- ======================================================

-- Users
INSERT INTO users (username, password, full_name, email, phone, role, active)
VALUES
('johndoe', '12345', 'John Doe', 'john@example.com', '0771234567', 'CUSTOMER', true),
('sara_cse', '12345', 'Sara Smith', 'sara@example.com', '0772345678', 'CUSTOMER_SERVICE_EXEC', true),
('mark_advisor', '12345', 'Mark Johnson', 'mark@example.com', '0773456789', 'SENIOR_ADVISOR', true),
('emma_finance', '12345', 'Emma Watson', 'emma@example.com', '0774567890', 'FINANCE_OFFICER', true),
('lucas_it', '12345', 'Lucas Brown', 'lucas@example.com', '0775678901', 'IT_ANALYST', true),
('anna_hr', '12345', 'Anna Taylor', 'anna@example.com', '0776789012', 'HR_ADMIN', true);

-- Customers
INSERT INTO customers (user_id, date_of_birth, national_id, occupation)
VALUES (1, '1990-05-10', '901234567V', 'Software Engineer');

-- Policies
INSERT INTO policies (customer_id, advisor_id, policy_type, start_date, end_date, status, sum_assured)
VALUES (1, 3, 'Life Coverage', '2025-01-01', '2045-01-01', 'ACTIVE', 5000000.00);

-- Beneficiaries
INSERT INTO beneficiaries (policy_id, name, relation, contact_info, share_percentage)
VALUES (1, 'Mary Doe', 'Spouse', 'mary@example.com', 100.00);

-- Risk Assessments
INSERT INTO risk_assessments (policy_id, advisor_id, assessment_date, risk_level, remarks, status)
VALUES (1, 3, '2025-01-02', 'LOW', 'Healthy individual, low risk', 'APPROVED');

-- Payments
INSERT INTO payments (policy_id, finance_officer_id, payment_date, amount, payment_type, status)
VALUES (1, 4, '2025-02-01', 20000.00, 'PREMIUM', 'COMPLETED');

-- Claims
INSERT INTO claims (policy_id, customer_id, submitted_date, status, remarks)
VALUES (1, 1, '2025-03-01', 'PENDING', 'Hospitalization expenses claim');

-- Claim Documents
INSERT INTO claim_documents (claim_id, document_type, file_path)
VALUES (1, 'Medical Report', '/uploads/claims/doc1.pdf');

-- System Logs
INSERT INTO system_logs (user_id, action, entity, entity_id)
VALUES (2, 'Created new customer profile', 'Customer', 1);

-- Policy Disputes
INSERT INTO policy_disputes (policy_id, customer_id, description, status)
VALUES (1, 1, 'Customer disputes policy coverage terms.', 'PENDING');