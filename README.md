# Online Banking System - Project Report

**Student Name:** Votha Sok  
**Course:** Spring Boot Development  
**Date:** [Submission Date]

## 1. Project Overview
This project implements a secure RESTful banking API using Spring Boot with JWT authentication. The system enables bank staff to manage user accounts and process financial transactions through a role-based access control system with three tiers: Admin, Manager, and User.

## 2. Technical Architecture
- **Backend Framework:** Spring Boot 3.1.0
- **Security:** Spring Security with JWT
- **Database:** PostgreSQL with JPA/Hibernate
- **Build Tool:** Gradle
- **API Documentation:** SpringDoc OpenAPI 3.0

## 3. Database Design Implementation
The system uses five core entities:
- **User:** Stores staff credentials with role associations
- **Role:** Defines user roles (ADMIN, MANAGER, USER)
- **Account:** Manages bank account information with auto-generated account numbers
- **Transaction:** Records all financial transfers with audit trails
- **RolePermission:** Implements many-to-many relationship between roles and permissions

## 4. JWT Authentication Flow
The authentication system works as follows:
1. User credentials are validated against the database
2. JWT token is generated containing username and role information
3. Token is included in Authorization header for subsequent requests
4. JwtAuthenticationFilter validates tokens on each request
5. User permissions are loaded into security context for authorization checks

## 5. API Endpoints Implementation
| Endpoint                     | Method | Description | Access |
|------------------------------|--------|-------------|---------|
| `/api/auth/admin/**`         | POST | User registration | Admin only |
| `/api/auth/login`            | POST | User login | Public |
| `/api/accounts`              | POST | Create bank account | Permission-based |
| `/api/transactions/transfer` | POST | Process transfers | Permission-based |

## 6. Security Implementation
- **Password Encryption:** BCrypt password encoder
- **Token Security:** JWT with configurable expiration
- **Method Security:** @PreAuthorize annotations for permission checks
- **CORS Configuration:** Configured for frontend integration

## 7. Key Features Implemented
- Automatic account number generation
- Transaction audit trails with user tracking
- Balance validation during transfers
- Role-based menu and functionality access
- Comprehensive error handling with meaningful messages

## 8. Technical Challenges & Solutions

**Challenge 1: JWT Integration with Spring Security**
*Problem:* Configuring custom JWT filter within Spring Security's filter chain.
*Solution:* Implemented JwtAuthenticationFilter and properly configured it in SecurityConfiguration to process tokens before standard authentication.

**Challenge 2: Transaction Atomicity**
*Problem:* Ensuring fund transfers are atomic operations.
*Solution:* Used @Transactional annotation to ensure both debit and credit operations complete successfully or roll back entirely.

**Challenge 3: Role-Permission Management**
*Problem:* Implementing flexible permission system.
*Solution:* Created many-to-many relationship between roles and permissions with efficient permission checking.

## 9. Testing Strategy
- Postman collection for API testing
- Role-based access control verification
- Edge case testing (insufficient funds, invalid accounts)
- Error handling validation

## 10. Future Enhancements
- Two-factor authentication implementation
- Transaction reporting and analytics
- Email notifications for transactions
- Mobile banking API support
- Microservices architecture migration

## 11. Learning Outcomes
- Practical experience with Spring Boot and Spring Security
- Deep understanding of JWT-based authentication
- RESTful API design best practices
- Database design with JPA/Hibernate
- Production-ready application configuration