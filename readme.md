# Library Management System

A simple library management REST API built with Spring Boot. Handles books, stock, users and loans, with JWT login.

## Tech used

- Java 25
- Spring Boot
- PostgreSQL
- Maven

## How to run

1. Clone the repo
```
git clone https://github.com/bhavyagaba-hue/Library.git
cd Library
```

2. Create a postgres database called `library`

3. Open `application.properties` and set your own postgres username/password

4. Run it
```
./mvnw spring-boot:run
```

app runs on `localhost:8080`.
