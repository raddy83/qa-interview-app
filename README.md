# QA Interview App

Spring Boot application for practicing Senior QA Automation Engineer interview questions.

The application allows importing questions from local JSON files and external APIs, solving randomized quizzes,
reviewing mistakes, and tracking interview preparation progress.

---

## Features

### Core Quiz

* Question browser
* Category filtering
* Random quiz mode (20 questions)
* Pass threshold: **75%**
* Result summary
* Review incorrect answers
* Display correct answers after quiz
* Explanation support

### Import Sources

* Import questions from JSON
* Import questions from Open Trivia API
* Import questions from StackOverflow API
* Randomized answer order

### Infrastructure

* H2 database
* Docker support
* GitHub Actions CI
* Automated test execution

---

## Tech Stack

Backend:

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven

Frontend:

* Thymeleaf
* HTML

Database:

* H2

DevOps:

* Docker
* Docker Compose
* GitHub Actions

Testing:

* JUnit 5
* Mockito
* Spring Boot Test

---

## Architecture

```text
Controller
↓
Service
↓
Repository
↓
Database
```

Application layers:

```text
src
├── main
│   ├── java
│   │   └── com.example.qa_interview_app
│   │       ├── controller
│   │       ├── service
│   │       ├── repository
│   │       ├── model
│   │       ├── dto
│   │       ├── session
│   │       └── config
│   │
│   └── resources
│       ├── templates
│       ├── questions
│       └── application.properties
│
└── test
    └── java
```

---

## Run Locally

Start application:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Open:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

---

## Run with Docker

Build and start:

```bash
docker compose up --build
```

Open:

```text
http://localhost:8080
```

Stop:

```bash
docker compose down
```

Rebuild:

```bash
docker compose build --no-cache
```

---

## Tests

Run all tests:

Windows:

```bash
mvnw.cmd clean test
```

Linux/macOS:

```bash
./mvnw clean test
```

Current automated coverage:

* QuestionService
* QuestionImportService
* QuizController
* OpenTriviaImportService
* StackOverflowImportService

---

## External API Integration

### Open Trivia API

Used to import multiple-choice questions.

Example flow:

```text
OpenTrivia API
↓
DTO mapping
↓
Question
↓
Database
```

Configuration:

```properties
integration.open-trivia.base-url=https://opentdb.com/api.php
```

---

### StackOverflow API

Imports highly voted questions and converts accepted answers into quiz options.

Example flow:

```text
StackOverflow
↓
Questions
↓
Answers
↓
DTO mapping
↓
Question
↓
Database
```

Configuration:

```properties
integration.stackoverflow.base-url=https://api.stackexchange.com/2.3/questions
integration.stackoverflow.question-answers-url=https://api.stackexchange.com/2.3/questions
integration.stackoverflow.answer-url=https://api.stackexchange.com/2.3/answers
integration.stackoverflow.site=stackoverflow
integration.stackoverflow.page-size=20
integration.stackoverflow.sort=votes
integration.stackoverflow.order=desc
integration.stackoverflow.answer-filter=withbody
```

Example tags:

```text
java
spring
selenium
docker
junit
rest
```

---

## Question Dataset

Default questions:

```text
src/main/resources/questions/questions.json
```

Current content:

```text
100+ Senior QA interview questions
```

---

## CI Pipeline

GitHub Actions automatically runs on:

* Push to main
* Pull Request to main

Pipeline:

```text
Checkout repository
↓
Setup Java
↓
Run tests
↓
Build application
↓
Build Docker image
```

Workflow:

```text
.github/workflows/ci.yml
```

---

## Database

Default configuration:

```text
H2 in-memory database
```

Hibernate:

```properties
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## Future Improvements

Planned roadmap:

* PostgreSQL
* Testcontainers
* Selenium E2E tests
* REST API
* Authentication
* Admin panel
* User accounts
* Statistics dashboard
* Kubernetes
* Deployment pipeline

---

Created as a portfolio project for improving:

* Spring Boot
* Testing
* Docker
* CI/CD
* API integrations
* Clean Architecture
* Backend interview preparation
