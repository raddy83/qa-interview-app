# QA Interview App

Spring Boot application for practicing Senior QA Automation Engineer interview questions.

## Features

* Spring Boot + Java
* H2 database
* Thymeleaf UI
* Question import from JSON
* Random 20-question quiz
* Pass threshold: 75%
* Category filtering
* Question explanations

## Stack

* Java 17
* Spring Boot
* Spring Data JPA
* H2
* Thymeleaf
* Maven

## Run

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

## Data

Questions are loaded from:

```text
src/main/resources/questions/questions.json
```

## Future Improvements

* Authentication
* Admin panel
* PostgreSQL
* REST API
* Docker
* CI/CD
* Selenium E2E tests
