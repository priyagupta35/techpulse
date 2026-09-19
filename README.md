# TechPulse 🚀

A hybrid technology news aggregator and community insights 
platform built with Java and Spring Boot.

TechPulse automatically fetches live technology articles from 
NewsAPI every 30 minutes, stores them in a PostgreSQL database, 
and serves them through secured REST APIs. Registered users can 
contribute their own technology insights which go through an 
admin moderation workflow before being published. The project 
evolved from a monolithic Spring Boot application into a 
deployed microservices architecture with AI-powered article 
summarisation and Docker containerisation.

## 🌐 Live Deployment

| Service | URL |
|---------|-----|
| News Delivery Service | https://news-delivery-service.onrender.com |
| News Ingestion Service | https://news-ingestion-service-3.onrender.com |

> Note — Render free tier services sleep after 15 minutes of 
> inactivity. First request may take 30 to 60 seconds to wake up.

**Quick Test**
GET https://news-delivery-service.onrender.com/api/articles

---

## 📌 Build Progress

| Technology | Status |
|-----------|--------|
| JDBC + MySQL | ✅ Done |
| Servlet and JSP | ✅ Done |
| Hibernate ORM | ✅ Done |
| Spring Boot + Spring Data JPA + REST API | ✅ Done |
| NewsAPI Integration + Community Module + Exception Handling | ✅ Done |
| Spring Security + JWT Authentication + Role-Based Access | ✅ Done |
| Log4j2 Structured Logging | ✅ Done |
| Microservices Architecture | ✅ Done |
| Spring AI + DeepSeek Article Summarisation | ✅ Done |
| Docker + Docker Compose | ✅ Done |
| Cloud Deployment on Render | ✅ Done |

---

## 🏗 Architecture
NewsAPI (External)
↓ every 30 minutes
News Ingestion Service (Port 8081)
↓ stores articles
PostgreSQL Database (Neon Cloud)
↑ reads data
News Delivery Service (Port 8080)
↓ secured REST APIs
Client (JWT Bearer Token required for protected endpoints)

**Three user roles**
- READER — view all approved articles and community posts
- CONTRIBUTOR — submit community posts in addition to READER access
- ADMIN — full access including moderation and news ingestion

---

## 🛠 Tech Stack

| Category | Technologies |
|----------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring MVC, Spring Data JPA, Spring Security |
| Database | PostgreSQL (Neon Cloud), Hibernate ORM |
| Security | JWT Authentication, BCrypt Password Hashing, RBAC |
| AI | Spring AI, Ollama, DeepSeek |
| Logging | Log4j2 with Console and Rolling File Appenders |
| Containerisation | Docker, Docker Compose |
| Deployment | Render Cloud Platform |
| External API | NewsAPI |
| Build Tool | Maven |
| Dev Tools | VS Code, Thunder Client, Git, GitHub |

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
| GET | `/api/articles/approved` | Get approved articles | Public |
| GET | `/api/articles/category/{id}` | Filter by category | Public |
| GET | `/api/articles/{id}/summary` | Get AI summary | Public |
| POST | `/api/articles` | Create new article | Authenticated |
| POST | `/api/articles/fetch` | Trigger NewsAPI ingestion | ADMIN |
| DELETE | `/api/articles/{id}` | Delete article | ADMIN |

### Community Posts
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/community-posts` | Get approved posts | Public |
| GET | `/api/community-posts/{id}` | Get post by ID | Public |
| POST | `/api/community-posts` | Submit new post | CONTRIBUTOR, ADMIN |
| PUT | `/api/community-posts/{id}/status?status=APPROVED` | Moderate post | ADMIN |

### Categories
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/categories` | Get all categories | Public |
| POST | `/api/categories` | Create category | Authenticated |

---

## 🗄 Database Schema
PostgreSQL (Neon Cloud)
├── categories — Article topics
├── sources — News outlets
├── articles — Core article entity
├── users — Platform users with roles
└── community_posts — User submitted content

Full schema available in `techpulse_db.sql` at the repository root.

---

## ⚙️ Local Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL or Neon account
- NewsAPI key from newsapi.org

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/priyagupta35/techpulse.git
cd techpulse
```

**2. Clone the microservices**
```bash
git clone https://github.com/priyagupta35/news-ingestion-service.git
git clone https://github.com/priyagupta35/news-delivery-service.git
```

**3. Configure application.properties in each service**
```properties
spring.datasource.url=jdbc:postgresql://your-neon-host/neondb?sslmode=require
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
newsapi.key=your_newsapi_key
newsapi.url=https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=
jwt.secret=your_jwt_secret_min_32_chars
jwt.expiration=86400000
```

**4. Run with Docker Compose**
```bash
docker-compose up --build
```

Or run each service individually.
```bash
cd news-ingestion-service && mvn spring-boot:run
cd news-delivery-service && mvn spring-boot:run
```

**5. Test the API**
```bash
POST http://localhost:8080/api/auth/register
{
    "username": "Your Name",
    "email": "you@example.com",
    "password": "password123",
    "role": "CONTRIBUTOR"
}
```
## 🔑 Key Features

**Live News Ingestion**
Automatically fetches technology articles from NewsAPI every 
30 minutes using Spring Scheduler. Duplicate detection using 
existsByUrl prevents saving the same article twice.

**AI Powered Summarisation**
Spring AI integrated with Ollama and DeepSeek generates 
concise 2 to 3 sentence summaries for any article via 
GET /api/articles/{id}/summary.

**Community Insights Module**
Contributors submit articles which start as PENDING. Admins 
approve or reject through a dedicated endpoint. Only APPROVED 
posts are publicly visible.

**JWT Authentication and RBAC**
Stateless JWT authentication with BCrypt password hashing. 
Three roles — Reader, Contributor, Admin — enforced across 
all endpoints by Spring Security.

**Microservices Architecture**
Split into two independent Spring Boot services communicating 
via REST. Each service is containerised with Docker and 
deployed independently on Render.

**Structured Logging**
Log4j2 logging across all service layers with DEBUG, INFO, 
WARN, and ERROR levels. Rolling file appender rotates logs 
daily for production-ready monitoring.

---
## 📁 Related Repositories

| Repository | Description | Live URL |
|-----------|-------------|---------|
| [techpulse](https://github.com/priyagupta35/techpulse) | Original monolith — Phases 1 to 7 | — |
| [news-ingestion-service](https://github.com/priyagupta35/news-ingestion-service) | Microservice for NewsAPI ingestion | https://news-ingestion-service-3.onrender.com |
| [news-delivery-service](https://github.com/priyagupta35/news-delivery-service) | Microservice for user-facing APIs | https://news-delivery-service.onrender.com |

---
Full schema available in `techpulse_db.sql` at the repository root.

## ⚙️ Local Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL or Neon account
- NewsAPI key from newsapi.org

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/priyagupta35/techpulse.git
cd techpulse
```

**2. Clone the microservices**
```bash
git clone https://github.com/priyagupta35/news-ingestion-service.git
git clone https://github.com/priyagupta35/news-delivery-service.git
```

**3. Configure application.properties in each service**
```properties
spring.datasource.url=jdbc:postgresql://your-neon-host/neondb?sslmode=require
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
newsapi.key=your_newsapi_key
newsapi.url=https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=
jwt.secret=your_jwt_secret_min_32_chars
jwt.expiration=86400000
```

**4. Run with Docker Compose**
```bash
docker-compose up --build
```

Or run each service individually.
```bash
cd news-ingestion-service && mvn spring-boot:run
cd news-delivery-service && mvn spring-boot:run
```

**5. Test the API**
```bash
POST http://localhost:8080/api/auth/register
{
    "username": "Your Name",
    "email": "you@example.com",
    "password": "password123",
    "role": "CONTRIBUTOR"
}
```

## 🔑 Key Features

**Live News Ingestion**
Automatically fetches technology articles from NewsAPI every 
30 minutes using Spring Scheduler. Duplicate detection using 
existsByUrl prevents saving the same article twice.

**AI Powered Summarisation**
Spring AI integrated with Ollama and DeepSeek generates 
concise 2 to 3 sentence summaries for any article via 
GET /api/articles/{id}/summary.

**Community Insights Module**
Contributors submit articles which start as PENDING. Admins 
approve or reject through a dedicated endpoint. Only APPROVED 
posts are publicly visible.

**JWT Authentication and RBAC**
Stateless JWT authentication with BCrypt password hashing. 
Three roles — Reader, Contributor, Admin — enforced across 
all endpoints by Spring Security.

**Microservices Architecture**
Split into two independent Spring Boot services communicating 
via REST. Each service is containerised with Docker and 
deployed independently on Render.

**Structured Logging**
Log4j2 logging across all service layers with DEBUG, INFO, 
WARN, and ERROR levels. Rolling file appender rotates logs 
daily for production-ready monitoring.


## 📁 Related Repositories

| Repository | Description | Live URL |
|-----------|-------------|---------|
| [techpulse](https://github.com/priyagupta35/techpulse) | Original monolith — Phases 1 to 7 | — |
| [news-ingestion-service](https://github.com/priyagupta35/news-ingestion-service) | Microservice for NewsAPI ingestion | https://news-ingestion-service-3.onrender.com |
| [news-delivery-service](https://github.com/priyagupta35/news-delivery-service) | Microservice for user-facing APIs | https://news-delivery-service.onrender.com |

## 👩‍💻 Author

**Priya Gupta**
B.Tech Computer Science and Business Systems
Netaji Subhash Engineering College, Kolkata

- GitHub: [@priyagupta35](https://github.com/priyagupta35)
- LinkedIn: [linkedin.com/in/priyagupta35](https://linkedin.com/in/priyagupta35)
  
