<div align="center">

# TechPulse 🚀
### Tech News Aggregation & Community Insights Platform

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6_JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon_Cloud-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://neon.tech/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Log4j2](https://img.shields.io/badge/Logging-Log4j2-E34F26?style=for-the-badge&logo=apache&logoColor=white)](https://logging.apache.org/log4j/2.x/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

<p align="center">
  A production-ready enterprise backend architecture engineered with <b>Spring Boot 3</b>, <b>Spring Security (JWT)</b>, and cloud-deployed with <b>PostgreSQL on Neon Serverless Cloud</b> (with local <b>MySQL 8.0</b> support).<br/>
  Features scheduled external data ingestion, automated deduplication, role-based access control (RBAC), community moderation workflows, and enterprise-grade Log4j2 rolling file logging.
</p>

---

</div>

## 📌 Architectural Overview

**TechPulse** is designed as a hybrid technology news platform that bridges automated external news scraping with a community submission pipeline:

1. **Automated News Ingestion Pipeline:** Runs an asynchronous Spring Scheduler cron job every 30 minutes, querying NewsAPI, checking for URL duplicates, resolving source metadata, and committing fresh articles to the database.
2. **Stateless Security & RBAC:** Enforces role-based access control across three user tiers (`READER`, `CONTRIBUTOR`, `ADMIN`) utilizing JSON Web Tokens (JJWT 0.11.5) and BCrypt password hashing.
3. **Cloud Database Architecture:** Backed by **Neon Serverless PostgreSQL** for cloud deployments and Render hosting, with full support for local containerized **MySQL 8.0**.
4. **Community Insights & Moderation:** Allows registered community contributors to submit original tech insights which enter a `PENDING` queue and require administrative approval before being published.
5. **Architectural Foundation for Microservices:** This repository houses the unified Spring Boot foundation, domain models, and database design. It subsequently evolved into decoupled microservices deployed on Render:
   - 📡 **[News Delivery Service](https://github.com/priyagupta35/news-delivery-service)** *(REST APIs + Spring AI with DeepSeek LLM on Neon PostgreSQL)*
   - 🔄 **[News Ingestion Service](https://github.com/priyagupta35/news-ingestion-service)** *(Standalone scheduled ingestion microservice)*
   - 🐳 **[TechPulse Docker](https://github.com/priyagupta35/techpulse-docker)** *(Multi-container Docker Compose orchestration)*

---

## 🏗️ System Architecture

```
                       ┌─────────────────────────┐
                       │   NewsAPI.org (HTTP)    │
                       └────────────┬────────────┘
                                    │ every 30 mins
                                    ▼
                       ┌─────────────────────────┐
                       │   NewsIngestionService  │
                       │   • Duplicate detection │
                       │   • Source resolution   │
                       └────────────┬────────────┘
                                    │
                                    ▼
┌─────────────────┐    ┌─────────────────────────┐    ┌─────────────────┐
│  Client Apps    │───>│  Security Filter Chain  │───>│ REST Controller │
│  (Thunder/Web)  │    │  • JwtFilter (Stateless)│    │ • Auth / Article│
└─────────────────┘    │  • BCrypt & RBAC        │    │ • CommunityPost │
                       └─────────────────────────┘    └────────┬────────┘
                                                               │
                                                               ▼
                                                      ┌─────────────────┐
                                                      │ Service Layer   │
                                                      └────────┬────────┘
                                                               │
                                                               ▼
                                                      ┌─────────────────┐
                                                      │ Spring Data JPA │
                                                      │ (Hibernate ORM) │
                                                      └────────┬────────┘
                                                               │
                                            ┌──────────────────┴──────────────────┐
                                            ▼                                     ▼
                               ┌─────────────────────────┐           ┌─────────────────────────┐
                               │  Neon Cloud PostgreSQL  │           │      Local MySQL 8      │
                               │   (Cloud Deployment)    │           │    (Local / Docker)     │
                               └─────────────────────────┘           └─────────────────────────┘
```

---

## 🛠️ Technology Stack

| Category | Component / Library | Details |
| :--- | :--- | :--- |
| **Language & Runtime** | Java 17 | Core programming language |
| **Framework** | Spring Boot 3.5.x | Spring MVC, Spring Data JPA, Spring Scheduling |
| **Security & Auth** | Spring Security 6 & JJWT 0.11.5 | Stateless JWT authentication, BCrypt, RBAC |
| **Cloud Database (Production)** | PostgreSQL on Neon Cloud | Serverless PostgreSQL cloud instance with connection pooling |
| **Local Database** | MySQL 8.0 | Relational database for local and Docker Compose workflows |
| **ORM / Data Access** | Hibernate 6.x / Spring Data JPA | Entity relationships (`@ManyToOne`, `@OneToMany`) |
| **Logging Framework** | Apache Log4j2 | Excluded default Logback; Daily rolling file & console appender |
| **External Integration**| NewsAPI | RESTful news ingestion via Spring `RestTemplate` |
| **Build & Tooling** | Apache Maven & Maven Wrapper | Project dependency management and lifecycle builds |

---

## 🔐 Role-Based Access Control (RBAC)

The application implements three explicit permission tiers:

| Role | Permissions & Capabilities |
| :--- | :--- |
| **`READER`** *(Public / Default)* | Read approved articles, browse categories, read approved community posts. |
| **`CONTRIBUTOR`** | All `READER` permissions + Submit original community posts for administrative review (`PENDING`). |
| **`ADMIN`** | Full platform access: Approve/Reject community posts, trigger manual NewsAPI ingestion, delete articles. |

---

## 🔌 API Specification

### 1. Authentication Endpoints (`/api/auth`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register user account with username, email, password, and role | Public |
| `POST` | `/api/auth/login` | Authenticate with email & password; returns Bearer JWT token | Public |

### 2. Articles Endpoints (`/api/articles`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/articles` | Retrieve all articles | Public |
| `GET` | `/api/articles/{id}` | Retrieve single article by ID | Public |
| `GET` | `/api/articles/approved` | Retrieve all approved articles | Public |
| `GET` | `/api/articles/category/{categoryId}` | Filter articles by category ID | Public |
| `POST` | `/api/articles` | Create a new article manually | Authenticated |
| `POST` | `/api/articles/fetch` | Manually trigger NewsAPI ingestion | `ROLE_ADMIN` |
| `DELETE` | `/api/articles/{id}` | Delete an article by ID | `ROLE_ADMIN` |

### 3. Community Posts Endpoints (`/api/community-posts`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/community-posts` | Retrieve all approved community posts | Public |
| `GET` | `/api/community-posts/{id}` | Retrieve single community post by ID | Public |
| `POST` | `/api/community-posts` | Submit new insight (Created as `PENDING`) | `ROLE_CONTRIBUTOR`, `ROLE_ADMIN` |
| `PUT` | `/api/community-posts/{id}/status?status=APPROVED` | Moderate post (`APPROVED` or `REJECTED`) | `ROLE_ADMIN` |

### 4. Categories Endpoints (`/api/categories`)
| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/categories` | List all available categories | Public |
| `POST` | `/api/categories` | Add a new article category | Authenticated |

---

## 📋 Sample Payloads

### User Registration (`POST /api/auth/register`)
```json
{
  "username": "priya_admin",
  "email": "priya@example.com",
  "password": "SecurePassword123",
  "role": "ADMIN"
}
```

### User Login (`POST /api/auth/login`)
```json
{
  "email": "priya@example.com",
  "password": "SecurePassword123"
}
```

### Submit Community Post (`POST /api/community-posts`)
```json
{
  "title": "Understanding Event-Driven Microservices in Spring",
  "content": "Event-driven architecture decouples services and enhances scalability...",
  "category": {
    "id": 4
  }
}
```

---

## 🗄️ Database Architecture & Schema

The relational schema is defined in [`techpulse_db.sql`](techpulse_db.sql):

```
techpulse_db
├── categories       (id [PK], name)
├── sources          (id [PK], name, website_url, country)
├── users            (id [PK], username, email [UQ], password, role [ENUM], created_at)
├── articles         (id [PK], title, summary, url, published_at, source_id [FK], category_id [FK], type [ENUM], status [ENUM])
└── community_posts  (id [PK], title, content, author_id [FK], category_id [FK], status [ENUM], created_at)
```

---

## 📝 Structured Logging with Log4j2

The application replaces standard Logback with **Apache Log4j2** (`src/main/resources/log4j2.xml`):
- **Console Appender:** Detailed formatted output for immediate local debugging.
- **Rolling File Appender:** Writes to `logs/techpulse.log`.
- **Automatic Rollover Policies:**
  - Daily rotation: `logs/techpulse-%d{yyyy-MM-dd}.log`.
  - Size-based trigger: Rotates when file size exceeds **10 MB**.
  - Retention Strategy: Automatically retains up to **30 days** of history.

---

## ⚙️ Setup & Configuration

### Prerequisites
- **Java 17+** (JDK)
- **Database:** Neon PostgreSQL account **OR** local MySQL 8.0
- **NewsAPI Key** (Free key from [newsapi.org](https://newsapi.org))
- Git

### 1. Clone the Repository
```bash
git clone https://github.com/priyagupta35/techpulse.git
cd techpulse
```

### 2. Configure Application Properties
Copy the example configuration:
```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

#### Option A: Running with Cloud Neon PostgreSQL (Recommended for Deployment)
```properties
spring.application.name=techpulse
server.port=8081

# Neon Serverless PostgreSQL
spring.datasource.url=jdbc:postgresql://your-neon-host/neondb?sslmode=require
spring.datasource.username=your_neon_username
spring.datasource.password=your_neon_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# NewsAPI & Security
newsapi.key=your_newsapi_key
newsapi.url=https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=
jwt.secret=your_super_secret_jwt_key_with_at_least_32_characters
jwt.expiration=86400000
```

#### Option B: Running with Local MySQL
```properties
spring.application.name=techpulse
server.port=8081

# Local MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/techpulse_db
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

# NewsAPI & Security
newsapi.key=your_newsapi_key
newsapi.url=https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=
jwt.secret=your_super_secret_jwt_key_with_at_least_32_characters
jwt.expiration=86400000
```

### 3. Build and Run
Using the included Maven Wrapper:

**Windows:**
```cmd
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8081`.

---

## 🌐 The Microservices Evolution

This monolithic foundation was subsequently evolved into production cloud microservices:

| Microservice Repository | Architecture & Responsibilities | Live URL |
| :--- | :--- | :--- |
| **[news-delivery-service](https://github.com/priyagupta35/news-delivery-service)** | User-facing REST API, JWT Authentication, and **Spring AI** (DeepSeek LLM) for automated article summaries. Deployed on Render with **Neon PostgreSQL**. | [Live API](https://news-delivery-service.onrender.com/api/articles) |
| **[news-ingestion-service](https://github.com/priyagupta35/news-ingestion-service)** | Standalone automated ingestion pipeline pulling live technology news from NewsAPI every 30 minutes into **Neon PostgreSQL**. | [Health Check](https://news-ingestion-service-3.onrender.com/api/ingestion/health) |
| **[techpulse-docker](https://github.com/priyagupta35/techpulse-docker)** | Multi-container Docker Compose configuration spinning up MySQL and all microservices locally in isolated containers. | — |

---

## 👩‍💻 Author

**Priya Gupta**  
*B.Tech in Computer Science & Business Systems (CSBS)*  
*Netaji Subhash Engineering College, Kolkata*

- **GitHub:** [@priyagupta35](https://github.com/priyagupta35)
- **LinkedIn:** [Priya Gupta](https://www.linkedin.com/in/priya-gupta-b4a79528a/)
- **Email:** [priyagupta.csbs@gmail.com](mailto:priyagupta.csbs@gmail.com)
