# Online Banking System

**Student Name:** Votha Sok  
**Course:** Spring Boot Development  
**Date:** [30/09/2025]

## 1. Project Overview

This project implements a secure RESTful banking API using Spring Boot with JWT authentication. The system enables bank
staff to manage user accounts and process financial transactions through a sophisticated role-based access control
system with feature-level permissions.

## 2. Technical Architecture

- **Backend Framework:** Spring Boot 3.1.0
- **Security:** Spring Security with JWT
- **Database:** PostgreSQL with JPA/Hibernate
- **Build Tool:** Gradle
- **API Documentation:** SpringDoc OpenAPI 3.0
- **Java Version:** 17

## 3. Database Design Implementation

The system core entities:

- **User :** Stores staff credentials with role associations
- **Role :** Defines user roles
- **Feature :** Defines user feature
- **Permission :** Defines feature permission
- **Account :** Manages bank account information with auto-generated account numbers
- **Transaction :** Records all financial transfers with audit trails
- **RolePermission :** Implements many-to-many relationship between roles and permissions

## 4. JWT Authentication Flow

The authentication system works as follows:

1. User credentials are validated against the database
2. JWT token is generated containing username and role information
3. Token is included in Authorization header for subsequent requests
4. JwtAuthenticationFilter validates tokens on each request
5. User permissions are loaded into security context for authorization checks
6. Method-level security annotations enforce feature-based access control

## 5. API Endpoints Implementation

### Authentication

| Method | Endpoint             | Description       | Access     |
|--------|----------------------|-------------------|------------|
| `POST` | `/api/auth/login`    | User login        | Public     |
| `POST` | `/api/auth/register` | User registration | Public     |

### User Management

| Method   | Endpoint          | Description    | Access |
|----------|-------------------|----------------|--------|
| `POST`   | `/api/users`      | Create user    | ADMIN  |
| `GET`    | `/api/users`      | Get all user   | ADMIN  |
| `GET`    | `/api/users/{id}` | Get user by ID | ADMIN  |
| `PUT`    | `/api/users/{id}` | Update user    | ADMIN  |
| `DELETE` | `/api/users/{id}` | Delete user    | ADMIN  |

### Account Management

| Method   | Endpoint             | Description         | Access |
|----------|----------------------|---------------------|--------|
| `POST`   | `/api/accounts`      | Create bank account | USER   |
| `GET`    | `/api/accounts`      | Get all accounts    | USER   |
| `GET`    | `/api/accounts/{id}` | Get account by ID   | USER   |
| `PUT`    | `/api/accounts/{id}` | Update account      | USER   |
| `DELETE` | `/api/accounts/{id}` | Delete account      | USER   |

### Transaction Management

| Method | Endpoint                     | Description          | Access  |
|--------|------------------------------|----------------------|---------|
| `POST` | `/api/transactions/transfer` | Process transfer     | MANAGER |
| `POST` | `/api/transactions/deposit`  | Process deposit      | MANAGER |
| `POST` | `/api/transactions/withdraw` | Process withdraw     | MANAGER |
| `GET`  | `/api/transactions/history`  | Get all transactions | MANAGER |

## 6. Security Implementation

- **Password Encryption:** BCrypt password encoder
- **Token Security:** JWT with configurable expiration
- **Method Security:** @PreAuthorize annotations for permission checks
- **CORS Configuration:** Configured for frontend integration
- **SQL Injection Prevention:** Parameterized queries with JPA
- **Role-Based Access Control:** Dynamic system role configuration

## 7. Key Features Implemented

- Automatic account number generation
- Transaction audit trails with user tracking
- Balance validation during transfers
- Role-based menu and functionality access
- Comprehensive error handling with meaningful messages

## 8. Technical Challenges & Solutions

**Challenge 1: JWT Integration with Spring Security**
*Problem:* Configuring custom JWT filter within Spring Security's filter chain.
*Solution:* Implemented JwtAuthenticationFilter and properly configured it in SecurityConfiguration to process tokens
before standard authentication.

**Challenge 2: Transaction Atomicity**
*Problem:* Ensuring fund transfers are atomic operations.
*Solution:* Used @Transactional annotation to ensure both debit and credit operations complete successfully or roll back
entirely.

**Challenge 3: Role-Permission Management**
*Problem:* Implementing flexible permission system.
*Solution:* Created many-to-many relationship between roles and permissions with efficient permission checking.

## 9. Testing Strategy

- Postman collection for API testing
- Role-based access control verification
- Edge case testing (insufficient funds, invalid accounts)
- Error handling validation

## 10. Learning Outcomes

- Practical experience with Spring Boot and Spring Security
- Deep understanding of JWT-based authentication
- RESTful API design best practices
- Database design with JPA/Hibernate
- Production-ready application configuration

## 11. Demo
Setup data for role-permission access
1. Create new database for demo db name `online_banking_demo`
2. Register User for super-admin user Email:  `superuser@gmail.com` Password: `super@123`
3. Run script insert features

    ```sql
    INSERT INTO features (id, created_date, menu_order, parent_id, router_link, status, title)
    VALUES (1, '2025-09-12 17:05:11.518738', 10, NULL, NULL, true, 'Setting'),
           (2, '2025-09-12 17:05:45.186496', 10, 1, '{setting/user}', true, 'User'),
           (3, '2025-09-12 17:06:07.010608', 15, 1, '{setting/role}', true, 'Role'),
           (4, '2025-09-19 15:18:26.417927', 15, 1, '{setting/account}', true, 'Account'),
           (5, '2025-09-19 15:18:45.865647', 10, NULL, NULL, true, 'Transaction'),
           (6, '2025-09-19 15:19:25.948104', 5, 5, '{transaction/transfer}', true, 'Transfer'),
           (7, '2025-09-19 15:19:39.467409', 10, 5, '{transaction/deposit}', true, 'Deposit'),
           (8, '2025-09-19 15:19:54.265739', 15, 5, '{transaction/withdraw}', true, 'Withdraw'),
           (9, '2025-09-20 10:48:05.216222', 20, 5, '{transaction/history}', true, 'History');
    ```
4. Run script insert permissions

   ```sql
   INSERT INTO permissions (id, created_date, function_name, function_order, status)
   VALUES (1, '2025-09-12 17:06:50.211713', 'Create', 1, true),
          (2, '2025-09-12 17:06:57.504835', 'View', 2, true),
          (3, '2025-09-12 17:07:05.587372', 'Update', 3, true),
          (4, '2025-09-12 17:07:13.971475', 'Delete', 4, true),
          (5, '2025-09-12 17:07:13.971475', 'Apply', 5, true);
   ```
5. Create Users
6. Create Roles
7. Apply role feature
8. Apply user role
9. Run script insert feature permission

   ```sql
   INSERT INTO feature_permissions (id, status, feature_id, permission_id)
   VALUES (1, true, 1, 2),
          (2, true, 1, 1),
          (3, true, 1, 4),
          (4, true, 1, 3),
          (5, true, 1, 5),
          (6, true, 2, 1),
          (7, true, 2, 2),
          (8, true, 2, 3),
          (9, true, 2, 4),
          (10, true, 2, 5),
          (11, true, 3, 1),
          (12, true, 3, 2),
          (13, true, 3, 3),
          (14, true, 3, 4),
          (17, true, 4, 2),
          (18, true, 4, 1),
          (19, true, 4, 4),
          (20, true, 4, 3),
          (21, true, 6, 1),
          (22, true, 7, 1),
          (23, true, 8, 1),
          (24, true, 9, 2),
          (25, true, 3, 5);
   ```
10. Process Transaction `Transfer`, `Deposit`, `Withdraw`
11. Process bulk `Transfer`, `Deposit`, `Withdraw` 200 request in a time 
