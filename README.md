# QA Interview App

Spring Boot application for practicing Senior QA Automation Engineer interview questions.

## Features

* Question import from JSON
* Category filtering
* Random quiz mode (20 questions)
* Pass threshold: 75%
* Explanations for answers
* H2 database
* Thymeleaf UI
* Docker support
* CI pipeline with GitHub Actions

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Thymeleaf
* H2
* Maven
* Docker
* GitHub Actions

---

## Project Structure

```text
src
├── main
│   ├── java
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── model
│   │   └── dto
│   └── resources
│       ├── templates
│       └── questions
│
└── test
```

---

## Run locally

Start application:

```bash
mvn spring-boot:run
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

---

## Tests

Run locally:

```bash
./mvnw test
```

Windows:

```bash
mvnw.cmd test
```

---

## CI Pipeline

Pipeline runs automatically on:

* push to main
* pull request to main

Pipeline steps:

```text
Checkout
↓
Setup Java
↓
Run Tests
↓
Build Maven
↓
Build Docker Image
```

---

## Question Source

Questions are loaded from:

```text
src/main/resources/questions/questions.json
```

Current dataset:

```text
100 Senior QA interview questions
```

---

## Future Improvements

* PostgreSQL
* Testcontainers
* Selenium E2E tests
* Admin panel
* Authentication
* Kubernetes
* Deployment
