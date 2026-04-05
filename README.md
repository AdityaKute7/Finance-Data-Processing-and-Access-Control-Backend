# Finance Data Processing and Access Control Backend

A robust Spring Boot backend application designed for managing finance records with comprehensive role-based access control (RBAC). It provides secure APIs for user management, financial data processing, and dashboard analytics.

## Features

- **JWT-Based Authentication & Authorization**: Secure login and registration with token-based authentication.
- **Role-Based Access Control (RBAC)**: Fine-grained access control with three distinct roles:
  - `ADMIN`: Full access to user management and CRUD operations on finance records.
  - `ANALYST`: Access to dashboard summaries and read-only access to records with advanced filtering.
  - `VIEWER`: Basic read-only access and filtering of finance records.
- **Finance Record Management**: Create, read, update, and delete financial records (Income, Expense, etc.).
- **Advanced Filtering**: Filter finance records by date ranges, categories, and types.
- **Dashboard Analytics**: Summarized dashboard data for analysts and admins.
- **PostgreSQL Database**: Relational database persistence using Spring Data JPA.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.5**
  - Spring Web (REST APIs)
  - Spring Data JPA
  - Spring Security
  - Spring Boot Validation
- **PostgreSQL** (Database)
- **JSON Web Tokens (jjwt)** (Authentication)
- **Lombok** (Boilerplate reduction)
- **Maven** (Build Tool)

## Project Structure

```
src/main/java/com/Aditya/ZorvynAsssignment
├── config/           # Application configuration classes
├── Controller/       # REST API Endpoints (Auth, Dashboard, FinanceRecord, User)
├── DTOs/             # Data Transfer Objects for API requests and responses
├── Entity/           # JPA Database Entities (User, Role, FinanceRecord, etc.)
├── Exception/        # Global exception handling logic
├── Repository/       # Spring Data JPA Repositories
├── security/         # JWT Filters, EntryPoints, Providers, and Security Config
└── Services/         # Business logic implementation
```

## Setup & Running the Application

### Prerequisites
- JDK 17
- PostgreSQL Server
- Maven

### Configuration
1. Open `src/main/resources/application.properties`.
2. Update the PostgreSQL data source configuration if necessary:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/FinanceDB
   spring.datasource.username=postgres
   spring.datasource.password=Aditya@123
   ```
3. Update the JWT Secret key (ensure it is secure in production):
   ```properties
   app.jwt.secret=YourSuperSecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLong!!
   ```

### Run the App
Navigate to the root directory `ZorvynAsssignment/` and use Maven wrapper to run the application:
```sh
./mvnw spring-boot:run
```
The application will start on `http://localhost:8080`.

## API Endpoints Summary

### Authentication API (`/api/auth`)
- `POST /register`: Register a new user (default role is generally handled by the request/logic).
- `POST /login`: Authenticate and receive a JWT token.

### User API (`/api/users`) - *Requires `ADMIN` role*
- `GET /`: Retrieve all registered users.
- `POST /`: Create a new user (with specific roles).

### Finance Records API (`/api/records`)
- `GET /`: Retrieve all records (Allowed for `VIEWER`, `ANALYST`, `ADMIN`).
- `POST /`: Create a new finance record (Allowed for `ADMIN`).
- `PUT /{id}`: Update an existing record (Allowed for `ADMIN`).
- `DELETE /{id}`: Delete a record (Allowed for `ADMIN`).
- `GET /filter/date`: Filter records by `startDate` and `endDate` (Allowed for `VIEWER`, `ANALYST`, `ADMIN`).
- `GET /filter/category`: Filter records by `category` (Allowed for `VIEWER`, `ANALYST`, `ADMIN`).
- `GET /filter/type`: Filter records by `type` (Allowed for `VIEWER`, `ANALYST`, `ADMIN`).

### Dashboard API (`/api/dashboard`)
- `GET /summary`: Retrieve financial summary and analytics (Allowed for `ANALYST`, `ADMIN`).

