TechPulse 🚀

> A hybrid technology news aggregator and community insights platform built with Java and Spring Boot.

TechPulse automatically fetches live technology articles from global news sources every 30 minutes, categorises them, and serves them through secured REST APIs. Registered users can contribute their own technology insights which go through an admin moderation workflow before being published. The platform is built progressively across a full Java backend curriculum — from raw JDBC through to a microservices architecture with Kafka, Spring AI, and Docker.

 🌐 Live Demo
> Deployment in progress — will be updated upon completion of Phase 12 Cloud Deployment.

>  📌 Project Status

This project is actively being developed alongside a comprehensive Java backend curriculum. Each phase introduces new technologies that are immediately applied to TechPulse.

| Phase | Technology | Status |
|-------|-----------|--------|
| Phase 1 | JDBC + MySQL | ✅ Complete |
| Phase 2 | Servlet & JSP | ✅ Complete (conceptual) |
| Phase 3 | Hibernate ORM | ✅ Complete |
| Phase 4 | Spring Boot + Spring Data JPA + REST API | ✅ Complete |
| Phase 5 | NewsAPI Integration + Community Module + Exception Handling | ✅ Complete |
| Phase 6 | Spring Security + JWT Authentication + Role-Based Access | ✅ Complete |
| Phase 7 | Log4j2 Structured Logging | ✅ Complete |

---

🛠 Tech Stack

** Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **Hibernate ORM**
- **REST API**

### Security
- **JWT Authentication** — stateless token-based auth
- **BCrypt** — password hashing
- **Role-Based Access Control** — READER, CONTRIBUTOR, ADMIN

### Database
- **MySQL 8.0**
- **Spring Data JPA repositories**
- **Hibernate ORM**

### Logging
- **Log4j2** — structured logging with console and rolling file appenders

### External Integrations
- **NewsAPI** — live technology news ingestion every 30 minutes

### Tools
- **Maven** — build and dependency management
- **Git + GitHub** — version control
- **VS Code** — development environment
- **Thunder Client** — API testing


## 🗄 Database Schema

```sql
techpulse_db
├── categories      — Article categories (AI, Cybersecurity, Cloud etc.)
├── sources         — News sources (TechCrunch, Wired etc.)
├── articles        — Core article entity with source and category relations
├── users           — Platform users with role-based access control
└── community_posts — User submitted technology content with moderation
```

## 🔌 API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and receive JWT token |

### Articles (Public GET, Protected POST/DELETE)
| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/articles` | Get all articles | Public |
| GET | `/api/articles/{id}` | Get article by ID | Public |
| GET | `/api/articles/approved` | Get approved articles | Public |
| GET | `/api/articles/category/{id}` | Filter by category | Public |
| POST | `/api/articles` | Create new article | Authenticated |
| POST | `/api/articles/fetch` | Trigger NewsAPI ingestion | ADMIN |
| DELETE | `/api/articles/{id}` | Delete article | ADMIN |

### Community Posts
| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/community-posts` | Get approved posts | Public |
| GET | `/api/community-posts/{id}` | Get post by ID | Public |
| POST | `/api/community-posts` | Submit new post | CONTRIBUTOR, ADMIN |
| PUT | `/api/community-posts/{id}/status?status=APPROVED` | Moderate post | ADMIN |

### Categories
| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/categories` | Get all categories | Public |
| POST | `/api/categories` | Create category | Authenticated |

---

## 🏗 Architecture
TechPulse
├── controller/     — REST API layer (HTTP request/response handling)
├── service/        — Business logic layer
├── repository/     — Data access layer (Spring Data JPA)
├── model/          — JPA entity classes
├── dto/            — Data Transfer Objects
├── security/       — JWT filter, JWT util, Security configuration
└── exception/      — Global exception handling

---

## ⚙️ Setup and Installation

### Prerequisites
- Java 17 or above
- Maven 3.8+
- MySQL 8.0
- NewsAPI key (free at newsapi.org)

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/priyagupta35/techpulse.git
cd techpulse
```

**2. Run the schema setup**

Import and execute the `techpulse_db.sql` file located in the repository root into your MySQL database.

Using MySQL command line:
```bash
mysql -u root -p < techpulse_db.sql
```

Or open the file in the VS Code MySQL extension and run it directly.
**3. Configure application.properties**

Create `src/main/resources/application.properties` with the following.

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

**5. Test the API**

The application starts on `http://localhost:8080`.

Register a user first.
```bash
POST http://localhost:8080/api/auth/register
{
    "username": "Your Name",
    "email": "you@example.com",
    "password": "password123",
    "role": "CONTRIBUTOR"
}
```

Use the returned token as Bearer token for protected endpoints.

Trigger live news ingestion (requires ADMIN token).
```bash
POST http://localhost:8080/api/articles/fetch
```

---

## 🔑 Key Features

**Live News Ingestion**
Automatically fetches technology articles from NewsAPI every 30 minutes using Spring's `@Scheduled` annotation. Duplicate detection prevents the same article from being saved twice.

**Community Insights Module**
Registered contributors can submit technology articles and opinions. All submissions start as PENDING and require admin approval before becoming publicly visible. Admins can approve or reject submissions through a dedicated moderation endpoint.

**JWT Authentication**
Stateless token-based authentication using the JJWT library. Tokens are signed with HS256 algorithm and expire after 24 hours. Every protected request is validated by a custom JwtFilter that runs before Spring Security's default filter chain.

**Role-Based Access Control**
Three distinct roles — READER for viewing content, CONTRIBUTOR for submitting community posts, and ADMIN for moderation and system management. Enforced through Spring Security's SecurityFilterChain configuration.

**Structured Logging**
Log4j2 logging across all service layers with appropriate log levels — DEBUG for development tracing, INFO for normal application events, WARN for unexpected but non-critical situations, ERROR for operation failures. Logs are written to both console and a rolling file appender that rotates daily.

**Global Exception Handling**
@ControllerAdvice based exception handler returns consistent, clean JSON error responses across all endpoints with appropriate HTTP status codes.

**Layered Architecture**
Clean separation of concerns across Controller, Service, and Repository layers following industry standard Spring Boot patterns.

---

## 📁 Project Structure
techpulse/
├── src/
│   └── main/
│       ├── java/com/techpulse/
│       │   ├── controller/
│       │   │   ├── ArticleController.java
│       │   │   ├── AuthController.java
│       │   │   ├── CategoryController.java
│       │   │   └── CommunityPostController.java
│       │   ├── service/
│       │   │   ├── ArticleService.java
│       │   │   ├── AuthService.java
│       │   │   ├── CommunityPostService.java
│       │   │   ├── NewsIngestionService.java
│       │   │   └── UserDetailsServiceImpl.java
│       │   ├── repository/
│       │   │   ├── ArticleRepository.java
│       │   │   ├── CategoryRepository.java
│       │   │   ├── CommunityPostRepository.java
│       │   │   ├── SourceRepository.java
│       │   │   └── UserRepository.java
│       │   ├── model/
│       │   │   ├── Article.java
│       │   │   ├── Category.java
│       │   │   ├── CommunityPost.java
│       │   │   ├── Source.java
│       │   │   └── User.java
│       │   ├── dto/
│       │   │   ├── AuthRequest.java
│       │   │   ├── AuthResponse.java
│       │   │   ├── NewsApiResponse.java
│       │   │   ├── NewsArticleDto.java
│       │   │   ├── NewsSourceDto.java
│       │   │   └── RegisterRequest.java
│       │   ├── security/
│       │   │   ├── JwtFilter.java
│       │   │   ├── JwtUtil.java
│       │   │   └── SecurityConfig.java
│       │   └── exception/
│       │       └── GlobalExceptionHandler.java
│       └── resources/
│           ├── application.properties (not committed — contains credentials)
│           └── log4j2.xml
├── logs/
│   └── techpulse.log (generated at runtime — not committed)
├── pom.xml



