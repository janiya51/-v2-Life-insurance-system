-- =========================
-- Database Creation
-- =========================
CREATE DATABASE IF NOT EXISTS LifeInsuranceDB;
USE LifeInsuranceDB;

-- =========================
-- 1. Users Table
-- =========================
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL, -- plain text (as per requirement)
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    role ENUM('CUSTOMER', 'CUSTOMER_SERVICE_EXEC', 'SENIOR_ADVISOR', 'FINANCE_OFFICER', 'IT_ANALYST', 'HR_ADMIN') NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================
-- 2. Customers Table
-- =========================
CREATE TABLE IF NOT EXISTS Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    date_of_birth DATE,
    national_id VARCHAR(50) UNIQUE,
    occupation VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- =========================
-- 3. Policies Table
-- =========================
CREATE TABLE IF NOT EXISTS Policies (
    policy_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    advisor_id INT,
    policy_type VARCHAR(100) NOT NULL,
    start_date DATE,
    end_date DATE,
    status ENUM('PENDING', 'ACTIVE', 'REJECTED', 'CANCELLED', 'EXPIRED') DEFAULT 'PENDING',
    sum_assured DECIMAL(12,2),
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (advisor_id) REFERENCES Users(user_id) ON DELETE SET NULL
);

-- =========================
-- 4. Beneficiaries Table
-- =========================
CREATE TABLE IF NOT EXISTS Beneficiaries (
    beneficiary_id INT AUTO_INCREMENT PRIMARY KEY,
    policy_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    relation VARCHAR(100),
    contact_info VARCHAR(200),
    share_percentage DECIMAL(5,2),
    FOREIGN KEY (policy_id) REFERENCES Policies(policy_id) ON DELETE CASCADE
);

-- =========================
-- 5. Risk Assessments Table
-- =========================
CREATE TABLE IF NOT EXISTS RiskAssessments (
    assessment_id INT AUTO_INCREMENT PRIMARY KEY,
    policy_id INT UNIQUE NOT NULL,
    advisor_id INT NOT NULL,
    assessment_date DATE NOT NULL,
    risk_level ENUM('LOW', 'MEDIUM', 'HIGH'),
    remarks TEXT,
    status ENUM('APPROVED', 'REJECTED', 'REVISION_REQUIRED') NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES Policies(policy_id) ON DELETE CASCADE,
    FOREIGN KEY (advisor_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- =========================
-- 6. Payments Table
-- =========================
CREATE TABLE IF NOT EXISTS Payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    policy_id INT NOT NULL,
    finance_officer_id INT,
    payment_date DATE NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_type ENUM('PREMIUM', 'BENEFIT') NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    FOREIGN KEY (policy_id) REFERENCES Policies(policy_id) ON DELETE CASCADE,
    FOREIGN KEY (finance_officer_id) REFERENCES Users(user_id) ON DELETE SET NULL
);

-- =========================
-- 7. Claims Table
-- =========================
CREATE TABLE IF NOT EXISTS Claims (
    claim_id INT AUTO_INCREMENT PRIMARY KEY,
    policy_id INT NOT NULL,
    customer_id INT NOT NULL,
    submitted_date DATE NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'UNDER_INVESTIGATION') DEFAULT 'PENDING',
    remarks TEXT,
    FOREIGN KEY (policy_id) REFERENCES Policies(policy_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE
);

-- =========================
-- 8. Claim Documents Table
-- =========================
CREATE TABLE IF NOT EXISTS ClaimDocuments (
    document_id INT AUTO_INCREMENT PRIMARY KEY,
    claim_id INT NOT NULL,
    document_type VARCHAR(100),
    file_path VARCHAR(255),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (claim_id) REFERENCES Claims(claim_id) ON DELETE CASCADE
);

-- =========================
-- 9. System Logs Table
-- =========================
CREATE TABLE IF NOT EXISTS SystemLogs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(200) NOT NULL,
    entity VARCHAR(100),
    entity_id INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL
);

-- =========================
-- 10. Policy Disputes Table (New CRUD for HR/Admin)
-- =========================
CREATE TABLE IF NOT EXISTS PolicyDisputes (
    dispute_id INT AUTO_INCREMENT PRIMARY KEY,
    policy_id INT NOT NULL,
    customer_id INT NOT NULL,
    description TEXT NOT NULL,
    status ENUM('PENDING', 'UNDER_REVIEW', 'RESOLVED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (policy_id) REFERENCES Policies(policy_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE
);

-- ======================================================
-- SAMPLE DATA INSERTS
-- ======================================================

-- Clear existing data (Safe update issue fixed by adding WHERE TRUE)
DELETE FROM PolicyDisputes WHERE TRUE;
DELETE FROM ClaimDocuments WHERE TRUE;
DELETE FROM SystemLogs WHERE TRUE;
DELETE FROM Claims WHERE TRUE;
DELETE FROM Payments WHERE TRUE;
DELETE FROM RiskAssessments WHERE TRUE;
DELETE FROM Beneficiaries WHERE TRUE;
DELETE FROM Policies WHERE TRUE;
DELETE FROM Customers WHERE TRUE;
DELETE FROM Users WHERE TRUE;

-- Reset auto-increment counters
ALTER TABLE Users AUTO_INCREMENT = 1;
ALTER TABLE Customers AUTO_INCREMENT = 1;
ALTER TABLE Policies AUTO_INCREMENT = 1;
ALTER TABLE Beneficiaries AUTO_INCREMENT = 1;
ALTER TABLE RiskAssessments AUTO_INCREMENT = 1;
ALTER TABLE Payments AUTO_INCREMENT = 1;
ALTER TABLE Claims AUTO_INCREMENT = 1;
ALTER TABLE ClaimDocuments AUTO_INCREMENT = 1;
ALTER TABLE SystemLogs AUTO_INCREMENT = 1;
ALTER TABLE PolicyDisputes AUTO_INCREMENT = 1;

-- Users
INSERT INTO Users (username, password, full_name, email, phone, role, active)
VALUES
('johndoe', '12345', 'John Doe', 'john@example.com', '0771234567', 'CUSTOMER', true),
('sara_cse', '12345', 'Sara Smith', 'sara@example.com', '0772345678', 'CUSTOMER_SERVICE_EXEC', true),
('mark_advisor', '12345', 'Mark Johnson', 'mark@example.com', '0773456789', 'SENIOR_ADVISOR', true),
('emma_finance', '12345', 'Emma Watson', 'emma@example.com', '0774567890', 'FINANCE_OFFICER', true),
('lucas_it', '12345', 'Lucas Brown', 'lucas@example.com', '0775678901', 'IT_ANALYST', true),
('anna_hr', '12345', 'Anna Taylor', 'anna@example.com', '0776789012', 'HR_ADMIN', true);

-- Customers
INSERT INTO Customers (user_id, date_of_birth, national_id, occupation)
VALUES (1, '1990-05-10', '901234567V', 'Software Engineer');

-- Policies
INSERT INTO Policies (customer_id, advisor_id, policy_type, start_date, end_date, status, sum_assured)
VALUES (1, 3, 'Life Coverage', '2025-01-01', '2045-01-01', 'ACTIVE', 5000000.00);

-- Beneficiaries
INSERT INTO Beneficiaries (policy_id, name, relation, contact_info, share_percentage)
VALUES (1, 'Mary Doe', 'Spouse', 'mary@example.com', 100.00);

-- Risk Assessments
INSERT INTO RiskAssessments (policy_id, advisor_id, assessment_date, risk_level, remarks, status)
VALUES (1, 3, '2025-01-02', 'LOW', 'Healthy individual, low risk', 'APPROVED');

-- Payments
INSERT INTO Payments (policy_id, finance_officer_id, payment_date, amount, payment_type, status)
VALUES (1, 4, '2025-02-01', 20000.00, 'PREMIUM', 'COMPLETED');

-- Claims
INSERT INTO Claims (policy_id, customer_id, submitted_date, status, remarks)
VALUES (1, 1, '2025-03-01', 'PENDING', 'Hospitalization expenses claim');

-- Claim Documents
INSERT INTO ClaimDocuments (claim_id, document_type, file_path)
VALUES (1, 'Medical Report', '/uploads/claims/doc1.pdf');

-- System Logs
INSERT INTO SystemLogs (user_id, action, entity, entity_id)
VALUES (2, 'Created new customer profile', 'Customer', 1);

-- Policy Disputes
INSERT INTO PolicyDisputes (policy_id, customer_id, description, status)
VALUES (1, 1, 'Customer disputes policy coverage terms.', 'PENDING');