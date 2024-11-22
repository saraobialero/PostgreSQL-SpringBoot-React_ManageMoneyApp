# Personal Finance Management App

A full-stack application for managing personal finances, built with Spring Boot, React, and PostgreSQL.

## 🏗️ Architecture Overview

### Database

- **Database**: PostgreSQL 15+

### Backend (Spring Boot)

- **Java Version**: 17
- **Spring Boot Version**: 3.2.x
- **Key Dependencies**:
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - Lombok
  - MapStruct

### Frontend (React)

- **Node Version**: 18+
- **Key Dependencies**:
  - React Query
  - React Router
  - Tailwind CSS
  - Shadcn/ui Components
  - Recharts (for financial charts)

## 🏛 Pillar 1: PostgreSQL Database

### Database Schema

- **Accounts**: Flexible system for managing different financial accounts
- **Transactions**: Income and expense tracking
- **Categories**: Customizable transaction categorization
- **Savings**: Savings goals management

### Key DB Features

- Multi-user support with permission management
- Many-to-many relationships for shared accounts
- Flexible categorization system
- Data validation triggers
- Optimized indexes for frequent queries

## 🚀 Pillar 2: Spring Boot Backend

### Backend Structure

```
src/main/java/com/financemanager/
├── config/
├── controller/
├── model/
├── repository/
├── service/
└── security/
```

### Backend Features

- Complete CRUD REST APIs
- Spring Security authentication
- Transactional management
- Data validation
- Logging and monitoring
- Unit and integration testing

## 💻 Pillar 3: React Frontend

### Frontend Structure

```
src/
├── components/
├── pages/
├── hooks/
├── services/
├── utils/
└── assets/
```

### Frontend Features

- Interactive dashboard
- Charts and statistics
- Validated forms
- Redux state management
- Dark/light theme
- Responsive design
- Jest testing

## 🗺 Roadmap

### Phase 1: MVP (Minimum Viable Product)

- [ ] Development environment setup
- [ ] Database schema implementation
- [ ] Basic Spring Boot APIs
- [ ] Essential React UI
- [ ] Authentication system

### Phase 2: Core Features

- [ ] Complete CRUD for all entities
- [ ] Dashboard with charts
- [ ] Notification system
- [ ] Report export
- [ ] Complete testing

### Phase 3: Advanced Features

- [ ] Electron integration for desktop version
- [ ] Offline synchronization
- [ ] Automatic transaction import
- [ ] Advanced budget planning
- [ ] Mobile responsive design
- [ ] User and role management for access control (v1.2)
- [ ] Access and refresh token for safe auth

### Phase 4: Optimizations

- [ ] Database performance tuning
- [ ] Caching implementation (v1.2)
- [ ] CI/CD pipeline (v1.2)
- [ ] Monitoring and analytics (v1.2)
- [ ] Complete API documentation

## 🛠 Setup & Installation

### Prerequisites

- PostgreSQL 14+
- Java 17+
- Node.js 18+
- npm 9+

### Database Setup

```bash
# Create database
psql -U postgres
CREATE DATABASE finance_manager;

# Run migrations
psql -U postgres -d finance_manager -f schema.sql
```

### Backend Setup

```bash
# Clone repository
git clone [repo-url]

# Build project
./mvnw clean install

# Run application
./mvnw spring-boot:run
```

### Frontend Setup

```bash
# Install dependencies
cd frontend
npm install

# Run development server
npm run dev
```

## 📝 License

Todo: create new license
