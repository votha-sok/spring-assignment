``# Online Banking System - Project Report

**Student Name:** Votha Sok  
**Course:** Spring Boot Development  
**Date:** [Submission Date]

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

The system uses five core entities:

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

### Authentication & User Management

| Method | Endpoint             | Description       | Access     |
|--------|----------------------|-------------------|------------|
| `POST` | `/api/auth/login`    | User login        | Public     |
| `POST` | `/api/auth/register` | User registration | ADMIN only |

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
- Production-ready application configuration``