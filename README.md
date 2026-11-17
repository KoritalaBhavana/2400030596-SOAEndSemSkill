# 📚 Library Management System – Microservices Integration Testing

## 1. Project Overview

The Library Management System is developed using a **Microservices Architecture** with Spring Boot and Spring Cloud. The system demonstrates communication and integration between multiple independent services through an API Gateway.

Although individual microservices operate independently, their interactions may introduce failures during communication, authentication, service discovery, and request distribution.

This project focuses on **Integration Testing** to validate end-to-end communication between different components and ensure that the complete workflow functions correctly.

The system incorporates:

- API Gateway for centralized request routing.
- Eureka Server for service discovery and registration.
- JWT-based authentication for secure API access.
- Multiple Book Service instances for load balancing.
- Integration testing to validate communication between services.

## 2. Objectives

The main objectives of this project are:

1. Implement a microservices-based Library Management System using Spring Boot.
2. Configure API Gateway for centralized request routing.
3. Implement service registration and discovery using Netflix Eureka.
4. Implement JWT-based authentication and token validation.
5. Configure multiple instances of Book Service.
6. Verify load balancing across multiple service instances.
7. Perform integration testing to identify communication failures.
8. Validate system behavior when one service instance becomes unavailable.

## 3. System Architecture

The system consists of four main components and multiple Book Service instances.

### Architecture Diagram

```text
                 CLIENT
                   |
                   |
              API GATEWAY
               Port 8080
                   |
          ---------------------
          |                   |
          |                   |
      AUTH SERVICE        BOOK SERVICE
       Port 8082              |
          |              ------------
          |              |          |
       JWT Token       Instance 1  Instance 2
       Generation       8081        8083
          |              |          |
          |              |          |
          --------------------------
                   |
              EUREKA SERVER
                Port 8761
```

### Component Description

| Component | Port | Responsibility |
|---|---:|---|
| Eureka Server | 8761 | Service registration and discovery |
| API Gateway | 8080 | Request routing |
| Auth Service | 8082 | Login and JWT generation |
| Book Service 1 | 8081 | Book management |
| Book Service 2 | 8083 | Additional service instance |

## 4. Technologies Used

### Backend Technologies

- Java 17
- Spring Boot
- Spring Cloud
- Spring Web
- Spring Security
- Spring Cloud Gateway (WebFlux)
- Netflix Eureka Server
- Eureka Discovery Client
- Spring Cloud LoadBalancer
- JJWT (Java JWT)

### Testing Tools

- JUnit 5
- Spring Boot Test
- MockMvc
- Postman / REST Client

### Development Tools

- Spring Tool Suite (STS)
- Maven
- Git
- GitHub

## 5. Microservices Description

### 5.1 Eureka Server

Eureka Server provides service discovery and registration.

Responsibilities:

- Maintains a registry of available microservices.
- Allows services to register themselves.
- Helps locate available service instances.
- Supports dynamic service discovery.

Dashboard:

`http://localhost:8761`

### 5.2 API Gateway

API Gateway acts as the single entry point for client requests.

Responsibilities:

- Receives incoming API requests.
- Routes requests to appropriate microservices.
- Uses Eureka for service discovery.
- Supports load balancing through service instances.

Gateway URL:

`http://localhost:8080`

Configured routes:

| Route | Destination |
|---|---|
| `/auth/**` | AUTH-SERVICE |
| `/books/**` | BOOK-SERVICE |

### 5.3 Auth Service

Auth Service handles user authentication and JWT token generation.

Responsibilities:

- Accepts user login credentials.
- Validates username and password.
- Generates JWT tokens.
- Provides token validation functionality.

Demo credentials:

| Field | Value |
|---|---|
| Username | admin |
| Password | admin123 |

**Note:** These are demonstration credentials implemented for the project. A production system should use database-backed users and securely hashed passwords.

### 5.4 Book Service

Book Service handles book-related operations.

Responsibilities:

- Fetches available books.
- Adds new books.
- Validates JWT tokens.
- Restricts unauthorized access.

Two instances are configured:

- Instance 1 – Port 8081
- Instance 2 – Port 8083

Both instances register with Eureka using the same service name.

## 6. JWT Authentication Workflow

JWT (JSON Web Token) is used to authenticate requests.

Workflow:

1. Client sends login credentials to Auth Service.
2. Auth Service verifies the credentials.
3. A JWT token is generated.
4. Client receives the JWT token.
5. Client sends the token in the Authorization header.
6. Book Service extracts the token.
7. JWT signature and expiration are validated.
8. Authorized requests are processed.

Authorization header:

```text
Authorization: Bearer <JWT_TOKEN>
```

## 7. API Endpoints

### Authentication APIs

**Login**

```http
POST http://localhost:8080/auth/login
```

Request Body:

```json
{
    "username": "admin",
    "password": "admin123"
}
```

Expected Response:

```json
{
    "token": "<generated-jwt-token>"
}
```

**Validate Token**

```http
GET http://localhost:8080/auth/validate?token=<JWT_TOKEN>
```

Expected Response:

```text
Token is valid
```

### Book Management APIs

**Get All Books**

```http
GET http://localhost:8080/books
```

Authorization:

```text
Bearer Token: <JWT_TOKEN>
```

Expected Response:

```text
Books fetched successfully from instance: 8081
```

**Add a Book**

```http
POST http://localhost:8080/books
```

Request Body:

```json
"Spring Boot Programming"
```

Expected Response:

```text
Book added successfully: Spring Boot Programming
```

## 8. Load Balancing

Spring Cloud LoadBalancer distributes incoming requests across available Book Service instances.

Configured instances:

| Instance | Port |
|---|---:|
| Book Service 1 | 8081 |
| Book Service 2 | 8083 |

Both instances register with Eureka under:

`BOOK-SERVICE`

When multiple requests are sent through API Gateway, they can be distributed across the available instances.

Example responses:

```text
Books fetched successfully from instance: 8081
Books fetched successfully from instance: 8083
```

This demonstrates load balancing between multiple service instances.

## 9. Integration Testing

Integration testing validates communication between different components of the microservices architecture.

The following five test cases are designed.

### Test Case 1: Valid Workflow

Steps:
1. Login using valid credentials.
2. Generate a JWT token.
3. Send a request to `/books` using the token.
4. Verify the response.

Expected Result: `200 OK`

### Test Case 2: Invalid Token

Steps:
1. Send a request to `/books`.
2. Provide an invalid JWT token.
3. Verify authentication failure.

Expected Result: `401 Unauthorized`

### Test Case 3: No Token

Steps:
1. Send a request to `/books`.
2. Do not provide an Authorization header.
3. Verify that access is denied.

Expected Result: `403 Forbidden`

### Test Case 4: Load Balancing Check

Steps:
1. Start both Book Service instances.
2. Send multiple requests through API Gateway.
3. Observe the instance identifier in the response.
4. Verify that requests reach different instances.

Expected Result: Requests are distributed between available instances.

### Test Case 5: Service Discovery Failure

Steps:
1. Start both Book Service instances.
2. Stop one Book Service instance.
3. Allow Eureka to update its registry.
4. Send requests through API Gateway.
5. Verify that the remaining instance handles requests.

Expected Result: `200 OK` from the remaining available instance.

## 10. Execution Procedure

Follow these steps to execute the project.

### Step 1: Start Eureka Server

Run Eureka Server as a Spring Boot application.

Verify:

`http://localhost:8761`

### Step 2: Start Auth Service

Run Auth Service on port 8082.

### Step 3: Start Book Service

Run the first Book Service instance on port 8081.

### Step 4: Start Second Book Service Instance

Run the same Book Service application with a different port:

```text
--server.port=8083
```

### Step 5: Start API Gateway

Run API Gateway on port 8080.

### Step 6: Verify Service Registration

Open the Eureka dashboard and verify that the services are registered.

### Step 7: Execute API Requests

Use Postman or a REST Client to execute authentication and book management requests.

### Step 8: Perform Integration Testing

Execute all five test scenarios and record the actual responses.

## 11. Expected Test Results

| Test Case | Expected Output |
|---|---|
| Valid Workflow | 200 OK |
| Invalid Token | 401 Unauthorized |
| No Token | 403 Forbidden |
| Load Balancing | Different service instances handle requests |
| Service Discovery Failure | Remaining instance handles requests |

All five scenarios should be verified through actual execution before marking the integration testing as successful.

## 12. Project Structure

```text
Library-Management-System/
│
├── EurekaServer/
│   ├── src/main/java/
│   ├── src/main/resources/
│   └── pom.xml
│
├── APIGateway/
│   ├── src/main/java/
│   │   └── com/library/gateway/
│   │       ├── ApiGatewayApplication.java
│   │       └── GatewayConfig.java
│   ├── src/main/resources/
│   └── pom.xml
│
├── AuthService/
│   ├── src/main/java/
│   │   └── com/library/auth/
│   │       ├── AuthServiceApplication.java
│   │       ├── AuthController.java
│   │       ├── JwtService.java
│   │       └── SecurityConfig.java
│   ├── src/main/resources/
│   └── pom.xml
│
├── BookService/
│   ├── src/main/java/
│   │   └── com/library/book/
│   │       ├── BookServiceApplication.java
│   │      
