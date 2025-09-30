# Banking System Data Dictionary

## Database Overview

- **Database Name**: Banking System
- **Description**: Comprehensive banking application with role-based access control, account management, and transaction processing
- **Version**: 1.0
- **Last Updated**: 2025-09-29

---

## Table: users

**Description**: Stores system user information and authentication details

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('users_id_seq') | Unique user identifier |
| user_name | VARCHAR(255) | NOT NULL | - | Username for login |
| email | VARCHAR(255) | NOT NULL, UNIQUE | - | User email address |
| password | VARCHAR(255) | NOT NULL | - | Encrypted password (bcrypt) |
| first_name | VARCHAR(255) | - | - | User's first name |
| last_name | VARCHAR(255) | - | - | User's last name |
| phone | VARCHAR(255) | UNIQUE | - | User's phone number |
| admin | BOOLEAN | - | - | Indicates if user has admin privileges |
| status | BOOLEAN | - | - | User account status (true=active) |
| created_date | TIMESTAMP | NOT NULL | - | Account creation timestamp |
| updated_date | TIMESTAMP | - | - | Last update timestamp |

**Indexes**:
- PRIMARY KEY (id)
- UNIQUE (email)
- UNIQUE (phone)
- idx_users_email
- idx_users_phone

---

## Table: roles

**Description**: Defines system roles and their descriptions

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('roles_seq') | Unique role identifier |
| name | VARCHAR(255) | - | - | Role name (Super Admin, MANAGER, ADMIN, USER) |
| description | VARCHAR(255) | - | - | Role description |
| status | BOOLEAN | - | - | Role status (true=active) |
| created_date | TIMESTAMP | NOT NULL | - | Role creation timestamp |
| updated_date | TIMESTAMP | - | - | Last update timestamp |

**Indexes**:
- PRIMARY KEY (id)

---

## Table: user_roles

**Description**: Maps users to their assigned roles (Many-to-Many relationship)

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('user_roles_id_seq') | Unique mapping identifier |
| user_id | BIGINT | FOREIGN KEY | - | Reference to users.id |
| role_id | BIGINT | FOREIGN KEY | - | Reference to roles.id |
| status | BOOLEAN | - | - | Mapping status (true=active) |

**Foreign Keys**:
- user_id → users(id)
- role_id → roles(id)

**Indexes**:
- PRIMARY KEY (id)
- idx_user_roles_user_id
- idx_user_roles_role_id

---

## Table: features

**Description**: Defines system features/menu items for role-based access control

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('features_id_seq') | Unique feature identifier |
| title | VARCHAR(255) | - | - | Feature title (Setting, User, Role, etc.) |
| icon | VARCHAR(255) | - | - | Feature icon reference |
| router_link | VARCHAR(255)[] | - | - | Angular/router links for navigation |
| menu_order | INTEGER | - | - | Display order in menu |
| parent_id | BIGINT | FOREIGN KEY | - | Reference to parent feature (self-referencing) |
| status | BOOLEAN | - | - | Feature status (true=active) |
| created_date | TIMESTAMP | NOT NULL | - | Feature creation timestamp |
| updated_date | TIMESTAMP | - | - | Last update timestamp |

**Foreign Keys**:
- parent_id → features(id)

**Indexes**:
- PRIMARY KEY (id)

---

## Table: permissions

**Description**: Defines CRUD operations and other permissions available in the system

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('permissions_id_seq') | Unique permission identifier |
| function_name | VARCHAR(255) | - | - | Permission name (Create, View, Update, Delete, Apply) |
| function_order | INTEGER | - | - | Display order for permissions |
| status | BOOLEAN | - | - | Permission status (true=active) |
| created_date | TIMESTAMP | NOT NULL | - | Permission creation timestamp |
| updated_date | TIMESTAMP | - | - | Last update timestamp |

**Indexes**:
- PRIMARY KEY (id)

---

## Table: feature_permissions

**Description**: Maps features to available permissions (Many-to-Many relationship)

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('feature_permissions_id_seq') | Unique mapping identifier |
| feature_id | BIGINT | FOREIGN KEY | - | Reference to features.id |
| permission_id | BIGINT | FOREIGN KEY | - | Reference to permissions.id |
| status | BOOLEAN | - | - | Mapping status (true=active) |

**Foreign Keys**:
- feature_id → features(id)
- permission_id → permissions(id)

**Indexes**:
- PRIMARY KEY (id)

---

## Table: role_feature

**Description**: Maps roles to accessible features (Many-to-Many relationship)

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('role_feature_id_seq') | Unique mapping identifier |
| role_id | BIGINT | FOREIGN KEY | - | Reference to roles.id |
| feature_id | BIGINT | FOREIGN KEY | - | Reference to features.id |
| status | BOOLEAN | - | - | Mapping status (true=active) |

**Foreign Keys**:
- role_id → roles(id)
- feature_id → features(id)

**Indexes**:
- PRIMARY KEY (id)

---

## Table: role_feature_permission

**Description**: Defines granular permissions for roles on specific features

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('role_feature_permission_id_seq') | Unique permission mapping identifier |
| role_id | BIGINT | FOREIGN KEY | - | Reference to roles.id |
| feature_id | BIGINT | FOREIGN KEY | - | Reference to features.id |
| permission_id | BIGINT | FOREIGN KEY | - | Reference to permissions.id |

**Foreign Keys**:
- role_id → roles(id)
- feature_id → features(id)
- permission_id → permissions(id)

**Indexes**:
- PRIMARY KEY (id)

---

## Table: accounts

**Description**: Stores bank account information and balances

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('accounts_id_seq') | Unique account identifier |
| account_number | VARCHAR(255) | NOT NULL, UNIQUE | - | Bank account number |
| account_holder_name | VARCHAR(255) | - | - | Name of account holder |
| account_holder_email | VARCHAR(255) | - | - | Account holder's email |
| account_holder_phone | VARCHAR(255) | - | - | Account holder's phone |
| national_id | VARCHAR(255) | - | - | National ID number |
| account_type | VARCHAR(255) | NOT NULL, CHECK | - | Account type: SAVINGS, CHECKING, BUSINESS |
| balance | NUMERIC(38,2) | NOT NULL | - | Current account balance |
| status | BOOLEAN | NOT NULL | - | Account status (true=active) |
| created_by | BIGINT | FOREIGN KEY | - | Reference to user who created the account |
| created_date | TIMESTAMP | NOT NULL | - | Account creation timestamp |
| updated_date | TIMESTAMP | - | - | Last update timestamp |

**Foreign Keys**:
- created_by → users(id)

**Constraints**:
- CHECK (account_type IN ('SAVINGS', 'CHECKING', 'BUSINESS'))

**Indexes**:
- PRIMARY KEY (id)
- UNIQUE (account_number)
- idx_accounts_number
- idx_accounts_holder_email

---

## Table: transactions

**Description**: Records all financial transactions in the system

| Field Name | Data Type | Constraints | Default | Description |
|------------|-----------|-------------|---------|-------------|
| id | BIGINT | PRIMARY KEY | nextval('transactions_id_seq') | Unique transaction identifier |
| transaction_id | VARCHAR(255) | NOT NULL, UNIQUE | - | System-generated transaction ID |
| amount | NUMERIC(19,4) | NOT NULL | - | Transaction amount |
| description | VARCHAR(255) | - | - | Transaction description |
| transaction_type | VARCHAR(255) | NOT NULL, CHECK | - | TRANSFER, DEPOSIT, WITHDRAW |
| transaction_status | VARCHAR(255) | NOT NULL, CHECK | - | PENDING, COMMITTED, FAILED, ROLLED_BACK |
| timestamp | TIMESTAMP | NOT NULL | - | Transaction timestamp |
| from_account_id | BIGINT | FOREIGN KEY | - | Source account (for TRANSFER/WITHDRAW) |
| to_account_id | BIGINT | FOREIGN KEY | - | Destination account (for TRANSFER/DEPOSIT) |
| processed_by_id | BIGINT | FOREIGN KEY | - | User who processed the transaction |

**Foreign Keys**:
- from_account_id → accounts(id)
- to_account_id → accounts(id)
- processed_by_id → users(id)

**Constraints**:
- CHECK (transaction_type IN ('TRANSFER', 'DEPOSIT', 'WITHDRAW'))
- CHECK (transaction_status IN ('PENDING', 'COMMITTED', 'FAILED', 'ROLLED_BACK'))

**Indexes**:
- PRIMARY KEY (id)
- UNIQUE (transaction_id)
- idx_transactions_timestamp
- idx_transactions_status
- idx_transactions_type

---

## Relationships Summary

1. **users** (1) ←→ (M) **user_roles** (M) ←→ (1) **roles**
2. **roles** (1) ←→ (M) **role_feature** (M) ←→ (1) **features**
3. **features** (1) ←→ (M) **feature_permissions** (M) ←→ (1) **permissions**
4. **roles** (1) ←→ (M) **role_feature_permission** (M) ←→ (1) **features**
5. **role_feature_permission** (M) ←→ (1) **permissions**
6. **users** (1) ←→ (M) **accounts** (created_by)
7. **accounts** (1) ←→ (M) **transactions** (from_account_id, to_account_id)
8. **users** (1) ←→ (M) **transactions** (processed_by_id)

---

## Enumeration Values

### Account Types
- `SAVINGS` - Savings account
- `CHECKING` - Checking account
- `BUSINESS` - Business account

### Transaction Types
- `TRANSFER` - Funds transfer between accounts
- `DEPOSIT` - Funds deposit to account
- `WITHDRAW` - Funds withdrawal from account

### Transaction Statuses
- `PENDING` - Transaction is pending processing
- `COMMITTED` - Transaction successfully completed
- `FAILED` - Transaction failed
- `ROLLED_BACK` - Transaction was rolled back

### Permission Functions
- `Create` - Create new records
- `View` - View records
- `Update` - Update existing records
- `Delete` - Delete records
- `Apply` - Apply/execute actions

---

## Security Notes

- Passwords are encrypted using bcrypt hashing
- Role-based access control (RBAC) implemented
- Granular permissions at feature-function level
- Audit trails maintained via created_date/updated_date
- Unique constraints on critical fields (email, phone, account_number)

---

## Sample Data Summary

### User Accounts
| Username | Role | Status |
|----------|------|--------|
| votha | Super Admin | Active |
| USER | USER | Active |
| ADMIN | ADMIN | Active |
| MANAGER | MANAGER | Active |

### Bank Accounts
| Account Number | Holder | Type | Balance |
|---------------|--------|------|---------|
| 9294275253 | SOK VOTHA | SAVINGS | $300.00 |
| 2497909593 | ROTHANAK | SAVINGS | $858.00 |

### Features Available
- Setting (Parent)
    - User Management
    - Role Management
    - Account Management
- Transaction (Parent)
    - Transfer
    - Deposit
    - Withdraw
    - History

---

## Database Schema Diagram
