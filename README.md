TechPulse 🚀

A hybrid technology news aggregator and community insights platform built with Java and Spring Boot.

TechPulse automatically fetches live technology articles from NewsAPI every 30 minutes, categorises them, and serves them through secured REST APIs. Registered users can contribute their own technology insights which go through an admin moderation workflow before being published. This project is being built progressively as I learn new backend technologies — each new concept is immediately applied to the codebase rather than kept as a separate tutorial exercise.

> 🌐 **Live Demo** — Coming soon. Will be updated once cloud deployment is complete.


## 📌 Build Progress

| Technology | Status |
|-----------|--------|
| JDBC + MySQL | ✅ Done |
| Servlet & JSP | ✅ Done |
| Hibernate ORM | ✅ Done |
| Spring Boot + Spring Data JPA + REST API | ✅ Done |
| NewsAPI Integration + Community Module + Exception Handling | ✅ Done |
| Spring Security + JWT Authentication + Role-Based Access | ✅ Done |
| Log4j2 Structured Logging | ✅ Done |


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


## 🗄 Database Schema

Five tables with proper foreign key relationships.
techpulse_db
├── categories      — Article categories (AI, Cybersecurity, Cloud, Software Development)
├── sources         — News sources (TechCrunch, Wired etc.)
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
| GET | `/api/articles/category/{id}` | Filter by category | Public |
| POST | `/api/articles` | Create new article | Authenticated |
| POST | `/api/articles/fetch` | Trigger live NewsAPI ingestion | ADMIN |
| DELETE | `/api/articles/{id}` | Delete article | ADMIN |

### Community Posts
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/community-posts` | Get all approved posts | Public |
| GET | `/api/community-posts/{id}` | Get post by ID | Public |
| POST | `/api/community-posts` | Submit new post | CONTRIBUTOR, ADMIN |
| PUT | `/api/community-posts/{id}/status?status=APPROVED` | Approve or reject post | ADMIN |

### Categories
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/categories` | Get all categories | Public |
| POST | `/api/categories` | Create new category | Authenticated |

---

## 🏗 How It Works
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

**Three user roles define what each user can do.**
- **READER** — can view all approved articles and community posts
- **CONTRIBUTOR** — can submit community posts in addition to READER access
- **ADMIN** — full access including content moderation, article management, and triggering news ingestion

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
```bash
mysql -u root -p < techpulse_db.sql
```

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

**5. Register a user and start testing**
```bash
POST http://localhost:8080/api/auth/register
{
    "username": "Your Name",
    "email": "you@example.com",
    "password": "password123",
    "role": "CONTRIBUTOR"
}
```

Use the returned JWT token as a Bearer token for all protected endpoints.

## 🔑 What Is Built So Far

**Live News Ingestion**
Every 30 minutes the application calls NewsAPI, fetches the latest technology articles, checks for duplicates, and saves new ones to the database automatically. No manual triggering needed — Spring's scheduler handles it in the background.

**Community Insights Module**
Anyone can read community posts but only registered contributors can submit them. Every new submission starts as PENDING. An admin reviews and either approves or rejects it before it becomes publicly visible. This reflects a real-world content moderation workflow.

**JWT Authentication and Role-Based Access**
Users register and log in to receive a JWT token. This token is sent with every request that requires authentication. A custom filter validates the token before the request reaches any endpoint. Depending on the user's role the request either proceeds or gets rejected with a 403.

**Structured Logging**
Every service class uses Log4j2 with appropriate log levels — INFO for normal events, WARN for skipped or unexpected data, ERROR for failures. Logs are saved to a rolling file that rotates daily, the same setup used in production environments.

**Clean Error Responses**
A global exception handler catches errors across all endpoints and returns consistent JSON responses instead of raw stack traces. Every error response includes a timestamp, status code, and a readable message.


## 🚀 What Is Coming Next Future Integrations

This project is still actively being worked on. Planned additions include splitting the application into independent microservices with a Kafka event-driven pipeline between them, integrating Spring AI for automatic article summarisation and community post categorisation, containerising the entire system with Docker and Docker Compose, and deploying both services to a cloud platform with a Jenkins CI/CD pipeline.

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

