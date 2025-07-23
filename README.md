# 🧩 SpringApp – Java Web App Onboarding Project

This is a step-by-step educational CRUD web application designed to help developers with no Java experience to get familiar with Java, Spring Boot, REST APIs, validation, exception handling, business logic, and database integration.

Key concepts covered:
- Java project structure (domain/service/controller layers)
- JPA entity mapping and Flyway migrations
- Request/response DTOs with validation
- MySQL integration via Docker
- Custom business logic (user/account/balance)
- Error handling via @ControllerAdvice
- Basic HTTP authentication with roles
- **Swagger/OpenAPI documentation**
- REST testing using Postman
- Unit testing with JUnit & Mockito

The project simulates a simplified banking system where users can register, manage a single currency account (EURO/LEVA), and perform deposits/withdrawals with currency conversion and rules enforcement.

## ✅ Step 1: Generate Project

Use Spring Initializr:
- Gradle (Groovy)
- Java 21
- **Spring Boot 3.2.5** (for compatibility)
- Group: com.example
- Artifact: springapp

Dependencies:
- Spring Web
- Spring Data JPA
- Validation
- MySQL Driver
- Spring Security

## ✅ Step 2: Folder Structure Setup

Create folders under `src/main/java/com/example/springapp/`:
```
├── controller
├── service
├── repository
├── domain
├── dto
├── exception
├── config
```

## 🐳 Step 3: Run MySQL with Docker

```bash
docker run --name springapp-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=springapp \
  -p 3306:3306 \
  -v springapp-mysql-data:/var/lib/mysql \
  -d mysql:8
```

## ✅ Step 4: Spring Configuration

- Use application.yaml
- Set correct MySQL JDBC URL
- Disable Hibernate DDL auto-creation
- Enable Flyway migrations

## ✅ Step 5: Add Swagger Documentation

Add SpringDoc OpenAPI dependency to `build.gradle`:
```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'
```

Create `OpenApiConfig.java` with BasicAuth security scheme configuration.

## 📘 Step 6: Flyway Database Structure

**Users Table**: id, name, email (unique), address
- Each user has multiple accounts

**Accounts Table**: id, user_id (FK), balance, currency (EURO or LEVA)
- Relationship: One User → Many Accounts

## ✅ Step 7: Define Entities

- Use @Entity, @Table, JPA annotations
- Use Lombok (@Data, @NoArgsConstructor)
- Keep fields consistent with Flyway
- **Add @JsonIgnore to prevent circular serialization**

## ✅ Step 8: Create Repositories

UserRepository, AccountRepository
- Extend JpaRepository

## 📦 Step 9: Create DTOs

- UserCreateRequest – validated user input
- UserResponse – returned after creation
- **AccountTransactionRequest with @DecimalMin validation**

## ✅ Step 10: User Service

- Handles registration logic
- Prevents duplicate emails
- Maps DTO → Entity
- Saves and returns UserResponse

## ✅ Step 11: User Controller

- Defines /users endpoint
- Accepts JSON, validates input
- Delegates to service
- Returns 201 Created
- **Add Swagger annotations (@Operation, @ApiResponses)**

## ✅ Step 12: Account Creation Business Logic

- Endpoint: POST /users/{userId}/accounts
- **Validation Rules:**
  - User can create only ONE account per currency (EURO or LEVA)
  - Auto-initialize balance to 0
  - Throw `IllegalArgumentException` if currency account already exists

## ✅ Step 13: Deposit & Withdrawal Business Logic

**Deposit Rules:**
- Minimum deposit: 5 EUR (validated in DTO and service)
- Maximum deposit: 5000 EUR ("More than 5k should be in bank")
- **Auto Currency Conversion:** 1 EUR = 2 LEVA
  - LEVA to EURO: divide by 2 (rounded to 2 decimals)
  - EURO to LEVA: multiply by 2

**Withdrawal Rules:**
- Minimum withdrawal: 5 EUR
- Cannot exceed account balance ("Insufficient funds")
- Same currency conversion logic as deposits

**Validation Layers:**
1. DTO validation: `@DecimalMin(value = "5.00")`
2. Service validation: Business rule checks
3. Exception handling: Proper HTTP status codes

## ✅ Step 14: Get Balance

- GET /users/{userId}/accounts/{accountId}/balance
- Returns JSON with balance, id, currency
- 404 if account not found or not linked to user

## ✅ Step 15: Global Exception Handler

Return proper HTTP codes:
- 400 for validation errors and business rule violations
- 404 for missing user/account (`EntityNotFoundException`)
- **403 for access denied (specific `AccessDeniedException` handler)**
- 500 for unexpected errors

## 🔐 Step 16: Security Configuration

Enable HTTP Basic Auth with **annotation-based security**:
- Use `@EnableMethodSecurity(prePostEnabled = true)`
- Define 2 users: admin (ADMIN), viewer (VIEWER)
- **Use @PreAuthorize annotations:**
  - `@PreAuthorize("hasRole('ADMIN')")` on POST /users
  - `@PreAuthorize("hasRole('ADMIN') or hasRole('VIEWER')")` on all AccountController methods
- Configure Swagger endpoints to be publicly accessible
- 401 if not authenticated, 403 if unauthorized

## 📖 Step 17: Test API Documentation

- Access Swagger UI at `/swagger-ui.html`
- Use BasicAuth credentials (admin/password, viewer/viewerpass)
- Test all business logic rules through Swagger UI

## 🧪 Step 18: Write Unit Tests

Cover business logic scenarios:
- User service: email uniqueness, entity creation
- Account service: currency validation, balance calculations
- Use mock repositories and @ExtendWith(MockitoExtension.class)

## 💼 Business Logic Summary

The application enforces these key business rules:
1. **One account per currency per user**
2. **Minimum transaction amount: 5 EUR**
3. **Maximum deposit: 5000 EUR**
4. **Automatic currency conversion (1 EUR = 2 LEVA)**
5. **Balance validation on withdrawals**
6. **Role-based access control**