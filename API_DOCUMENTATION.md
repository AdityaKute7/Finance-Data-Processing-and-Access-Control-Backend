# API Documentation

This document provides detailed information about all REST API endpoints available in the Finance Data Processing Backend.

> [!NOTE]
> All secured endpoints require an HTTP `Authorization` header containing a valid Bearer Token. Format: `Authorization: Bearer <your-jwt-token>`

## 1. Authentication API
Base Path: `/api/auth`
Public endpoints for registering and logging into the system.

### Register a User
- **URL:** `/register`
- **Method:** `POST`
- **Access:** Public

**Request Body (`RegisterRequest`)**
```json
{
  "username": "johndoe",
  "email": "johndoe@example.com",
  "password": "securepassword123",
  "role": "VIEWER" // Optional, used when an Admin assigns a role. Can be VIEWER, ANALYST, ADMIN
}
```

**Response (`AuthResponse`)** - `200 OK`
```json
{
  "token": "eyJhbGciOiJIUz...",
  "username": "johndoe",
  "role": "ROLE_VIEWER"
}
```

### Login
- **URL:** `/login`
- **Method:** `POST`
- **Access:** Public

**Request Body (`LoginRequest`)**
```json
{
  "username": "johndoe",
  "password": "securepassword123"
}
```

**Response (`AuthResponse`)** - `200 OK`
```json
{
  "token": "eyJhbGciOiJIUz...",
  "username": "johndoe",
  "role": "ROLE_VIEWER"
}
```

---

## 2. User API
Base Path: `/api/users`
Endpoints for managing users. 

> [!IMPORTANT]
> All `/api/users` endpoints require the **ADMIN** role.

### Get All Users
- **URL:** `/`
- **Method:** `GET`
- **Access:** `ADMIN`

**Response (`List<UserDto>`)** - `200 OK`
```json
[
  {
    "id": 1,
    "username": "adminUser",
    "email": "admin@example.com",
    "role": "ROLE_ADMIN",
    "status": "ACTIVE"
  }
]
```

### Create User
- **URL:** `/`
- **Method:** `POST`
- **Access:** `ADMIN`

**Request Body (`RegisterRequest`)**
```json
{
  "username": "analystUser",
  "email": "analyst@example.com",
  "password": "password123",
  "role": "ANALYST"
}
```

**Response (`UserDto`)** - `201 Created`
```json
{
  "id": 2,
  "username": "analystUser",
  "email": "analyst@example.com",
  "role": "ROLE_ANALYST",
  "status": "ACTIVE"
}
```

---

## 3. Finance Records API
Base Path: `/api/records`
Endpoints for managing and filtering finance records.

### Object Schemas
**`FinanceRecordRequest`**
```json
{
  "amount": 1500.50, // Must be > 0.01
  "type": "INCOME", // Type can be INCOME or EXPENSE
  "category": "Salary", // Max 50 chars
  "date": "2024-10-15", // yyyy-mm-dd
  "description": "October Salary" // Max 255 chars
}
```
**`FinanceRecordResponse`**
```json
{
  "id": 101,
  "amount": 1500.50,
  "type": "INCOME",
  "category": "Salary",
  "date": "2024-10-15",
  "description": "October Salary",
  "createdByUsername": "adminUser",
  "createdAt": "2024-10-15T14:30:00"
}
```

### Get All Records
- **URL:** `/`
- **Method:** `GET`
- **Access:** `VIEWER`, `ANALYST`, `ADMIN`
- **Response:** `200 OK` with `List<FinanceRecordResponse>`

### Create a Record
- **URL:** `/`
- **Method:** `POST`
- **Access:** `ADMIN`
- **Request Body:** `FinanceRecordRequest`
- **Response:** `201 Created` with `FinanceRecordResponse`

### Update a Record
- **URL:** `/{id}`
- **Method:** `PUT`
- **Access:** `ADMIN`
- **Request Body:** `FinanceRecordRequest`
- **Response:** `200 OK` with `FinanceRecordResponse`

### Delete a Record
- **URL:** `/{id}`
- **Method:** `DELETE`
- **Access:** `ADMIN`
- **Response:** `204 No Content`

### Filter by Date
- **URL:** `/filter/date`
- **Method:** `GET`
- **Access:** `VIEWER`, `ANALYST`, `ADMIN`
- **Query Params:** 
  - `startDate` (ISO Date e.g., `2024-01-01`)
  - `endDate` (ISO Date e.g., `2024-12-31`)
- **Response:** `200 OK` with `List<FinanceRecordResponse>`

### Filter by Category
- **URL:** `/filter/category`
- **Method:** `GET`
- **Access:** `VIEWER`, `ANALYST`, `ADMIN`
- **Query Params:** `category` (String)
- **Response:** `200 OK` with `List<FinanceRecordResponse>`

### Filter by Type
- **URL:** `/filter/type`
- **Method:** `GET`
- **Access:** `VIEWER`, `ANALYST`, `ADMIN`
- **Query Params:** `type` (`INCOME` or `EXPENSE`)
- **Response:** `200 OK` with `List<FinanceRecordResponse>`

---

## 4. Dashboard API
Base Path: `/api/dashboard`
Analytical and summarized views of the financial data.

### Get Summary
- **URL:** `/summary`
- **Method:** `GET`
- **Access:** `ANALYST`, `ADMIN`

**Response (`DashboardSummary`)** - `200 OK`
```json
{
  "totalIncome": 5000.00,
  "totalExpenses": 1200.00,
  "netBalance": 3800.00,
  "recentActivity": [
    { /* FinanceRecordResponse */ }
  ],
  "categoryTotals": {
    "Salary": 5000.00,
    "Groceries": 400.00,
    "Utilities": 800.00
  },
  "monthlySummary": [
    {
      "month": "2024-10",
      "income": 5000.00,
      "expense": 1200.00
    }
  ]
}
```
