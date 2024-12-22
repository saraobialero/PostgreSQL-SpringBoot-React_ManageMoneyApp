
# Money Manager - Backend Documentation

## Overview
REST API backend for a personal finance management application, developed with Spring Boot and PostgreSQL. The application is designed for local use without authentication, focusing on simplicity and functionality.

## Technologies
- Spring Boot 3.4.0
- OpenAPI (Swagger) for API documentation
- Spring Data JPA

## Environment Setup

### Prerequisites
- Java JDK 21
- PostgreSQL 14+
- Maven

### Database Setup
The system uses two separate databases for different environments:
- `managemoney_dev`: Development environment
- `managemoney_prod`: Production environment

#### Database Initialization
```sql
CREATE DATABASE managemoney_dev;
CREATE DATABASE managemoney_prod;
```

### Environment Configuration
The application uses separate YAML configuration files for each environment:

- `application-dev.yaml`: Development configuration
- `application-prod.yaml`: Production configuration

### Environment Variables
Configure the following environment variables:
```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=managemoney_dev
DB_USERNAME=money_manager
DB_PASSWORD=your_password
SHOW_SQL=true
HIBERNATE_DDL=none
SERVER_PORT=8080
```

## Execution Profiles
The application supports two profiles:

### Development Profile
```bash
SPRING_PROFILES_ACTIVE=dev
```
Features:
- SQL logging enabled
- Hibernate ddl-auto: update
- Log level: DEBUG
- Schema auto-update enabled

### Production Profile
```bash
SPRING_PROFILES_ACTIVE=prod
```
Features:
- SQL logging disabled
- Hibernate ddl-auto: validate
- Log level: INFO
- Schema validation only

## API Documentation
API documentation available through Swagger UI:
- URL: `http://localhost:8080/api/swagger-ui.html`
- API Docs: `http://localhost:8080/api/api-docs`

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/app/manage_money/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       ├── application-dev.yaml
│       └── application-prod.yaml
```

## Development Notes
- No authentication implemented (local use application)
- Swagger used for testing and API documentation
- Separate environment management through Spring profiles
- Environment-specific configurations through YAML files

## How to Run

### Development Mode
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Mode
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Database Schema
The application uses a PostgreSQL database with the following schema:
- Accounts management
- Transaction tracking
- Labels and categories
- Recurring transactions
- Saving plans

Detailed schema documentation can be found in `schema.sql`.

## API Endpoints
API documentation with all available endpoints and their usage is accessible through the Swagger UI interface when the application is running.

## Configuration Files

### Development Configuration (`application-dev.yaml`)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:managemoney_dev}
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
```

### Production Configuration (`application-prod.yaml`)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:managemoney_prod}
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
```

## Logging
Logging is configured differently for each profile:
- Development: Detailed logging with SQL statements
- Production: Essential logging only
