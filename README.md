# TechPulse 🚀

A hybrid technology news aggregator and community insights platform built with Java and Spring Boot.

TechPulse automatically fetches live technology articles from NewsAPI every 30 minutes, categorises them, and serves them through secured REST APIs. Registered users can contribute their own technology insights which go through an admin moderation workflow before being published. The platform is built progressively across a full Java backend curriculum — from raw JDBC through to a microservices architecture with Kafka, Spring AI, and Docker.

> 🌐 **Live Demo** — Deployment in progress. Will be updated upon completion of Phase 12 Cloud Deployment.

---

## 📌 Project Status

This project is actively being developed alongside a comprehensive Java backend curriculum. Each phase introduces new technologies that are immediately applied to TechPulse.

| Phase | Technology | Status |
|-------|-----------|--------|
| Phase 1 | JDBC + MySQL | ✅ Complete |
| Phase 2 | Servlet & JSP | ✅ Complete |
| Phase 3 | Hibernate ORM | ✅ Complete |
| Phase 4 | Spring Boot + Spring Data JPA + REST API | ✅ Complete |
| Phase 5 | NewsAPI Integration + Community Module + Exception Handling | ✅ Complete |
| Phase 6 | Spring Security + JWT Authentication + Role-Based Access | ✅ Complete |
| Phase 7 | Log4j2 Structured Logging | ✅ Complete |
| Phase 8 | Microservices Architecture | ⏳ Upcoming |
| Phase 9 | Apache Kafka | ⏳ Upcoming |
| Phase 10 | Spring AI + DeepSeek | ⏳ Upcoming |
| Phase 11 | Docker + Docker Compose | ⏳ Upcoming |
| Phase 12 | Cloud Deployment + Jenkins CI/CD | ⏳ Upcoming |

---

## 🛠 Tech Stack

| Category | Technologies |
|----------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring MVC, Spring Data JPA, Spring Security |
| Database | MySQL 8.0, Hibernate ORM |
| Security | JWT Authentication, BCrypt Password Hashing, Role-Based Access Control |
| Logging | Log4j2 with Console and Rolling File Appenders |
| External API | NewsAPI — live technology news ingestion |
| Build Tool | Maven |
| Dev Tools | VS Code, Thunder Client, Git, GitHub |
| Upcoming | Apache Kafka, Spring AI, Docker, Jenkins, Cloud Deployment |

---

## 🗄 Database Schema

The database consists of five tables with proper relational integrity.   
techpulse_db
├── categories      — Article categories (AI, Cybersecurity, Cloud, Software Development)
├── sources         — News sources (TechCrunch, Wired etc.) with website and country
├── articles        — Core article entity linked to source and category
├── users           — Platform users with BCrypt hashed passwords and role assignment
└── community_posts — User submitted content with PENDING/APPROVED/REJECTED workflow

Full schema available in `techpulse_db.sql` at the repository root.

---

## 🔌 API Endpoints

### Authentication
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login and receive JWT token | Public |

### Articles
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/articles` | Get all articles | Public |
| GET | `/api/articles/{id}` | Get article by ID | Public |
| GET | `/api/articles/approved` | Get approved articles only | Public |
| GET | `/api/articles/category/{id}` | Filter articles by category | Public |
| POST | `/api/articles` | Create new article | Authenticated |
| POST | `/api/articles/fetch` | Trigger live NewsAPI ingestion | ADMIN |
| DELETE | `/api/articles/{id}` | Delete article | ADMIN |

### Community Posts
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/community-posts` | Get all approved posts | Public |
| GET | `/api/community-posts/{id}` | Get post by ID | Public |
| POST | `/api/community-posts` | Submit new community post | CONTRIBUTOR, ADMIN |
| PUT | `/api/community-posts/{id}/status?status=APPROVED` | Approve or reject post | ADMIN |

### Categories
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/categories` | Get all categories | Public |
| POST | `/api/categories` | Create new category | Authenticated |

---

## 🏗 Architecture

TechPulse follows a clean three-layer architecture.
HTTP Request
↓
JwtFilter — validates Bearer token on every request
↓
Controller — handles HTTP request and response
↓
Service — contains all business logic
↓
Repository — Spring Data JPA, talks to MySQL
↓
JSON Response

**Three user roles define access levels.**
- **READER** — can view all approved articles and community posts
- **CONTRIBUTOR** — can submit community posts in addition to READER permissions
- **ADMIN** — full access including moderation, article management, and news ingestion

---

## ⚙️ Setup and Installation

### Prerequisites
- Java 17 or above
- Maven 3.8+
- MySQL 8.0
- NewsAPI key — free registration at newsapi.org

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/priyagupta35/techpulse.git
cd techpulse
```

**2. Set up the database**

Run the provided SQL file to create the database, all tables, and seed data.
```bash
mysql -u root -p < techpulse_db.sql
```

**3. Configure application.properties**

Create `src/main/resources/application.properties` with the following content.
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/techpulse_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.open-in-view=false
server.port=8080
newsapi.key=your_newsapi_key
newsapi.url=https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=
jwt.secret=TechPulseSecretKeyForJWTTokenGenerationAndValidation2026
jwt.expiration=86400000
```

**4. Run the application**
```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

**5. Quick start test**

Register a user and get your JWT token.
```bash
POST http://localhost:8080/api/auth/register
{
    "username": "Your Name",
    "email": "you@example.com",
    "password": "password123",
    "role": "CONTRIBUTOR"
}
```

Use the returned token as a Bearer token for all protected endpoints.

---

## 🔑 Key Features

**Live News Ingestion**
Fetches technology articles from NewsAPI automatically every 30 minutes using Spring's `@Scheduled` annotation. Includes duplicate detection to prevent the same article being saved multiple times.

**Community Insights Module**
Contributors can submit their own technology articles and opinions. Every submission starts as PENDING and requires admin approval before becoming publicly visible. Admins moderate through a dedicated endpoint.

**JWT Authentication and Role-Based Access**
Stateless authentication using signed JWT tokens. Every request passes through a custom JwtFilter that validates the token before it reaches any controller. Three roles — READER, CONTRIBUTOR, and ADMIN — enforce granular access control across all endpoints.

**Structured Logging with Log4j2**
Every service layer has proper Log4j2 logging with DEBUG, INFO, WARN, and ERROR levels used appropriately. Logs are written to both the console during development and a rolling file appender that rotates daily for production use.

**Global Exception Handling**
A `@ControllerAdvice` based exception handler intercepts all exceptions and returns clean, consistent JSON error responses with appropriate HTTP status codes instead of raw stack traces.

---

## 📁 Project Structure
techpulse/
├── src/main/java/com/techpulse/
│   ├── controller/         — REST endpoints
│   ├── service/            — Business logic
│   ├── repository/         — Spring Data JPA interfaces
│   ├── model/              — JPA entity classes
│   ├── dto/                — Data Transfer Objects
│   ├── security/           — JWT filter, JWT util, Security config
│   └── exception/          — Global exception handler
├── src/main/resources/
│   ├── application.properties  (not committed — contains credentials)
│   └── log4j2.xml
├── logs/                   (generated at runtime — not committed)
├── techpulse_db.sql        — Complete database schema and seed data
├── pom.xml
└── README.md

