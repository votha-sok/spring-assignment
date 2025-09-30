-- =============================================
-- Banking System Database Creation Script
-- PostgreSQL
-- =============================================

-- Drop existing tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS role_feature_permission CASCADE;
DROP TABLE IF EXISTS role_feature CASCADE;
DROP TABLE IF EXISTS feature_permissions CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS features CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;

-- Drop sequences if they exist
DROP SEQUENCE IF EXISTS accounts_id_seq CASCADE;
DROP SEQUENCE IF EXISTS feature_permissions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS features_id_seq CASCADE;
DROP SEQUENCE IF EXISTS permissions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS role_feature_id_seq CASCADE;
DROP SEQUENCE IF EXISTS role_feature_permission_id_seq CASCADE;
DROP SEQUENCE IF EXISTS roles_seq CASCADE;
DROP SEQUENCE IF EXISTS transactions_id_seq CASCADE;
DROP SEQUENCE IF EXISTS user_roles_id_seq CASCADE;
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;

-- =============================================
-- CREATE SEQUENCES
-- =============================================

CREATE SEQUENCE accounts_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE feature_permissions_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE features_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE permissions_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE role_feature_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE role_feature_permission_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE roles_seq START 1 INCREMENT 50;
CREATE SEQUENCE transactions_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE user_roles_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE users_id_seq START 1 INCREMENT 1;

-- =============================================
-- CREATE TABLES
-- =============================================

-- Users table
CREATE TABLE users (
                       id BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
                       user_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       phone VARCHAR(255) UNIQUE,
                       admin BOOLEAN,
                       status BOOLEAN,
                       created_date TIMESTAMP NOT NULL,
                       updated_date TIMESTAMP
);

-- Roles table
CREATE TABLE roles (
                       id BIGINT PRIMARY KEY DEFAULT nextval('roles_seq'),
                       name VARCHAR(255),
                       description VARCHAR(255),
                       status BOOLEAN,
                       created_date TIMESTAMP NOT NULL,
                       updated_date TIMESTAMP
);

-- User-Roles mapping table
CREATE TABLE user_roles (
                            id BIGINT PRIMARY KEY DEFAULT nextval('user_roles_id_seq'),
                            user_id BIGINT REFERENCES users(id),
                            role_id BIGINT REFERENCES roles(id),
                            status BOOLEAN
);

-- Features table (Menu items)
CREATE TABLE features (
                          id BIGINT PRIMARY KEY DEFAULT nextval('features_id_seq'),
                          title VARCHAR(255),
                          icon VARCHAR(255),
                          router_link VARCHAR(255)[],
                          menu_order INTEGER,
                          parent_id BIGINT REFERENCES features(id),
                          status BOOLEAN,
                          created_date TIMESTAMP NOT NULL,
                          updated_date TIMESTAMP
);

-- Permissions table (CRUD operations)
CREATE TABLE permissions (
                             id BIGINT PRIMARY KEY DEFAULT nextval('permissions_id_seq'),
                             function_name VARCHAR(255),
                             function_order INTEGER,
                             status BOOLEAN,
                             created_date TIMESTAMP NOT NULL,
                             updated_date TIMESTAMP
);

-- Feature-Permissions mapping table
CREATE TABLE feature_permissions (
                                     id BIGINT PRIMARY KEY DEFAULT nextval('feature_permissions_id_seq'),
                                     feature_id BIGINT REFERENCES features(id),
                                     permission_id BIGINT REFERENCES permissions(id),
                                     status BOOLEAN
);

-- Role-Feature mapping table
CREATE TABLE role_feature (
                              id BIGINT PRIMARY KEY DEFAULT nextval('role_feature_id_seq'),
                              role_id BIGINT REFERENCES roles(id),
                              feature_id BIGINT REFERENCES features(id),
                              status BOOLEAN
);

-- Role-Feature-Permission mapping table
CREATE TABLE role_feature_permission (
                                         id BIGINT PRIMARY KEY DEFAULT nextval('role_feature_permission_id_seq'),
                                         role_id BIGINT REFERENCES roles(id),
                                         feature_id BIGINT REFERENCES features(id),
                                         permission_id BIGINT REFERENCES permissions(id)
);

-- Accounts table
CREATE TABLE accounts (
                          id BIGINT PRIMARY KEY DEFAULT nextval('accounts_id_seq'),
                          account_number VARCHAR(255) NOT NULL UNIQUE,
                          account_holder_name VARCHAR(255),
                          account_holder_email VARCHAR(255),
                          account_holder_phone VARCHAR(255),
                          national_id VARCHAR(255),
                          account_type VARCHAR(255) NOT NULL CHECK (
                              account_type IN ('SAVINGS', 'CHECKING', 'BUSINESS')
                              ),
                          balance NUMERIC(38,2) NOT NULL,
                          status BOOLEAN NOT NULL,
                          created_by BIGINT REFERENCES users(id),
                          created_date TIMESTAMP NOT NULL,
                          updated_date TIMESTAMP
);

-- Transactions table
CREATE TABLE transactions (
                              id BIGINT PRIMARY KEY DEFAULT nextval('transactions_id_seq'),
                              transaction_id VARCHAR(255) NOT NULL UNIQUE,
                              amount NUMERIC(19,4) NOT NULL,
                              description VARCHAR(255),
                              transaction_type VARCHAR(255) NOT NULL CHECK (
                                  transaction_type IN ('TRANSFER', 'DEPOSIT', 'WITHDRAW')
                                  ),
                              transaction_status VARCHAR(255) NOT NULL CHECK (
                                  transaction_status IN ('PENDING', 'COMMITTED', 'FAILED', 'ROLLED_BACK')
                                  ),
                              timestamp TIMESTAMP NOT NULL,
                              from_account_id BIGINT REFERENCES accounts(id),
                              to_account_id BIGINT REFERENCES accounts(id),
                              processed_by_id BIGINT REFERENCES users(id)
);

-- =============================================
-- INSERT DATA: Users
-- =============================================
INSERT INTO users (id, admin, created_date, email, password, phone, status, user_name) VALUES
                                                                                           (1, true, '2025-09-12 10:43:42.61304', 'vothasok@example.com',
                                                                                            '{bcrypt}$2a$10$nOPBflA1XELYUjesj3nZzeN1o/oSXms7AYxO1GR6MFhk.N7RSAqxC', '015600022', true, 'votha'),
                                                                                           (3, false, '2025-09-29 13:24:13.392613', 'user@gmail.com',
                                                                                            '{bcrypt}$2a$10$OXlAJM9aFM.2evRf7lcdDeSTpIJDB0IaFaJsjfewMNTcLNY0JU9PC', '010800900', true, 'USER'),
                                                                                           (4, false, '2025-09-29 13:37:07.961021', 'admin@gmail.com',
                                                                                            '{bcrypt}$2a$10$e1fo1lxJ16N4F6JqGyZfDustJZCkRPYA7mLdYh9eAq89jr7FL/LBS', '0908998880', true, 'ADMIN'),
                                                                                           (5, false, '2025-09-29 15:12:40.603996', 'manager@mail.com',
                                                                                            '{bcrypt}$2a$10$jPjS5nWhep9N6EqH8IJvquGR64FtqWTUpcd1ynjP4ID.8ZqEVP8QO', '070890890', true, 'MANAGER');

-- =============================================
-- INSERT DATA: Roles
-- =============================================
INSERT INTO roles (id, created_date, description, name, status) VALUES
                                                                    (1, '2025-09-12 17:13:28.167379', 'Role for super admin', 'Super Admin', true),
                                                                    (2, '2025-09-12 17:13:36.698434', 'Role for manager', 'MANAGER', true),
                                                                    (3, '2025-09-12 17:13:36.698434', 'ADMIN role', 'ADMIN', true),
                                                                    (4, '2025-09-12 17:13:36.698434', 'User role', 'USER', true);

-- =============================================
-- INSERT DATA: User Roles
-- =============================================
INSERT INTO user_roles (id, status, role_id, user_id) VALUES
                                                          (1, true, 1, 1),
                                                          (2, true, 4, 3),
                                                          (3, true, 3, 4),
                                                          (4, true, 2, 5);

-- =============================================
-- INSERT DATA: Features
-- =============================================
INSERT INTO features (id, created_date, menu_order, parent_id, router_link, status, title) VALUES
                                                                                               (1, '2025-09-12 17:05:11.518738', 10, NULL, NULL, true, 'Setting'),
                                                                                               (2, '2025-09-12 17:05:45.186496', 10, 1, '{setting/user}', true, 'User'),
                                                                                               (3, '2025-09-12 17:06:07.010608', 15, 1, '{setting/role}', true, 'Role'),
                                                                                               (4, '2025-09-19 15:18:26.417927', 15, 1, '{setting/account}', true, 'Account'),
                                                                                               (5, '2025-09-19 15:18:45.865647', 10, NULL, NULL, true, 'Transaction'),
                                                                                               (6, '2025-09-19 15:19:25.948104', 5, 5, '{transaction/transfer}', true, 'Transfer'),
                                                                                               (7, '2025-09-19 15:19:39.467409', 10, 5, '{transaction/deposit}', true, 'Deposit'),
                                                                                               (8, '2025-09-19 15:19:54.265739', 15, 5, '{transaction/withdraw}', true, 'Withdraw'),
                                                                                               (9, '2025-09-20 10:48:05.216222', 20, 5, '{transaction/history}', true, 'History');

-- =============================================
-- INSERT DATA: Permissions
-- =============================================
INSERT INTO permissions (id, created_date, function_name, function_order, status) VALUES
                                                                                      (1, '2025-09-12 17:06:50.211713', 'Create', 1, true),
                                                                                      (2, '2025-09-12 17:06:57.504835', 'View', 2, true),
                                                                                      (3, '2025-09-12 17:07:05.587372', 'Update', 3, true),
                                                                                      (4, '2025-09-12 17:07:13.971475', 'Delete', 4, true),
                                                                                      (5, '2025-09-12 17:07:13.971475', 'Apply', 5, true);

-- =============================================
-- INSERT DATA: Feature Permissions
-- =============================================
INSERT INTO feature_permissions (id, status, feature_id, permission_id) VALUES
                                                                            (1, true, 1, 2), (2, true, 1, 1), (3, true, 1, 4), (4, true, 1, 3), (5, true, 1, 5),
                                                                            (6, true, 2, 1), (7, true, 2, 2), (8, true, 2, 3), (9, true, 2, 4), (10, true, 2, 5),
                                                                            (11, true, 3, 1), (12, true, 3, 2), (13, true, 3, 3), (14, true, 3, 4), (17, true, 4, 2),
                                                                            (18, true, 4, 1), (19, true, 4, 4), (20, true, 4, 3), (21, true, 6, 1), (22, true, 7, 1),
                                                                            (23, true, 8, 1), (24, true, 9, 2), (25, true, 3, 5);

-- =============================================
-- INSERT DATA: Role Features
-- =============================================
INSERT INTO role_feature (id, status, feature_id, role_id) VALUES
                                                               (57, true, 4, 4), (58, true, 1, 4), (61, true, 2, 3), (62, true, 3, 3), (63, true, 1, 3),
                                                               (64, true, 5, 2), (65, true, 6, 2), (66, true, 7, 2), (67, true, 8, 2), (68, true, 9, 2);

-- =============================================
-- INSERT DATA: Role Feature Permissions
-- =============================================
INSERT INTO role_feature_permission (id, feature_id, permission_id, role_id) VALUES
                                                                                 (20, 4, 4, 4), (21, 4, 1, 4), (22, 4, 2, 4), (23, 4, 3, 4),
                                                                                 (52, 7, 1, 2), (53, 6, 1, 2), (54, 9, 2, 2), (55, 8, 1, 2),
                                                                                 (56, 2, 4, 3), (57, 2, 1, 3), (58, 2, 2, 3), (59, 2, 5, 3), (60, 2, 3, 3),
                                                                                 (61, 3, 4, 3), (62, 3, 1, 3), (63, 3, 2, 3), (64, 3, 3, 3), (65, 3, 5, 3);

-- =============================================
-- INSERT DATA: Accounts
-- =============================================
INSERT INTO accounts (id, account_holder_email, account_holder_name, account_holder_phone,
                      account_number, account_type, balance, created_date, national_id, status, updated_date, created_by) VALUES
                                                                                                                              (1, 'votha7777@gmail.com', 'SOK VOTHA', '015600022', '9294275253', 'SAVINGS', 0.00,
                                                                                                                               '2025-09-15 14:26:51.291256', '3890823', true, '2025-09-29 15:37:41.344057', 1),
                                                                                                                              (2, 'rotanak@gmail.com', 'ROTHANAK', '0709809000', '2497909593', 'SAVINGS', 0.00,
                                                                                                                               '2025-09-19 15:21:02.930032', NULL, true, '2025-09-29 15:37:41.333409', 1);


-- =============================================
-- UPDATE SEQUENCES
-- =============================================
SELECT setval('accounts_id_seq', 2, true);
SELECT setval('features_id_seq', 9, true);
SELECT setval('permissions_id_seq', 5, true);
SELECT setval('feature_permissions_id_seq', 25, true);
SELECT setval('roles_seq', 51, true);
SELECT setval('role_feature_id_seq', 68, true);
SELECT setval('role_feature_permission_id_seq', 65, true);
SELECT setval('users_id_seq', 5, true);
SELECT setval('user_roles_id_seq', 4, true);
SELECT setval('transactions_id_seq', 935, true);

-- =============================================
-- CREATE INDEXES for better performance
-- =============================================
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_accounts_number ON accounts(account_number);
CREATE INDEX idx_accounts_holder_email ON accounts(account_holder_email);
CREATE INDEX idx_transactions_timestamp ON transactions(timestamp);
CREATE INDEX idx_transactions_status ON transactions(transaction_status);
CREATE INDEX idx_transactions_type ON transactions(transaction_type);
CREATE INDEX idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);

-- =============================================
-- FINAL CONFIRMATION
-- =============================================
SELECT 'Banking System Database created and populated successfully!' AS message;

-- Display table counts for verification
SELECT
    'Users: ' || COUNT(*)::TEXT AS users_count FROM users
UNION ALL
SELECT 'Roles: ' || COUNT(*)::TEXT AS roles_count FROM roles
UNION ALL
SELECT 'Accounts: ' || COUNT(*)::TEXT AS accounts_count FROM accounts
UNION ALL
SELECT 'Transactions: ' || COUNT(*)::TEXT AS transactions_count FROM transactions;