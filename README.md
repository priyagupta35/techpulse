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
