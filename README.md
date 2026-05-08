# Running Weather App

A Spring Boot web application that helps runners find suitable training times based on weather conditions, recovery status, and personal training preferences.

The application combines hourly weather forecasts with individual training goals and fatigue factors to generate personalized running recommendations such as:

> “Today at 18:00 — 18 °C, light wind and sunny conditions — good time for a run.”

---

## Features

* Personalized training time recommendations
* Hourly weather forecast visualization
* Weather-based scoring system
* Fatigue and recovery tracking
* Weekly training goals
* Run documentation and history
* Calendar view for completed activities
* Location autocomplete via Geocoding API
* AJAX-based dynamic frontend updates

---

## Technologies

### Backend

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* Hibernate
* PostgreSQL

### Frontend

* Thymeleaf
* HTML / CSS
* JavaScript
* AJAX

### External APIs

* Open-Meteo Weather API
* Open-Meteo Geocoding API

---

## Architecture & Design Concepts

Detailed documentation of development:
https://drive.google.com/file/d/1J2NgvBwgCBODDJbwIhwRwKSGX88X-s75/view?usp=drive_link

The project follows a layered architecture consisting of:

* Controller layer
* Service layer
* Repository layer
* Domain model layer

Additionally, several software design principles and patterns were applied:

* Adapter Pattern
* Builder Pattern
* Facade Pattern
* Strategy Pattern (extended scoring strategies)
* Aggregator Pattern
* DTOs (Data Transfer Objects)
* Value Objects (DDD-inspired)
* Single Responsibility Principle
* Domain Model Pattern

The recommendation system is modular and extensible:
different scoring strategies independently evaluate weather, fatigue, and training preferences before their scores are aggregated into a final recommendation score.

---

## Recommendation System

The recommendation engine works in multiple stages:

1. Generate possible training hour candidates
2. Evaluate each candidate using multiple scoring strategies
3. Aggregate all scores into a final score
4. Filter low-quality recommendations
5. Return the best recommendations per day

Factors considered include:

* Temperature
* Rain probability
* Wind speed
* Cloud coverage
* Daylight
* Recovery/fatigue level
* Preferred training time

---

## Database

The application uses PostgreSQL together with Spring Data JPA and Hibernate.

Sensitive database credentials are not included in this repository.

Configure your own database connection in:

```properties
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/runningweather
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

---

## Running the Application

### Requirements

* Java 21
* Maven
* PostgreSQL

### Start the application

```bash
mvn spring-boot:run
```

The application will then be available at:

```text
http://localhost:8080
```

---

## Screenshots

<img width="424" height="816" alt="grafik" src="https://github.com/user-attachments/assets/265fe2ed-406a-4577-a087-8d75a77aeb0c" />




---

## Learning Outcomes

This project was developed as part of a university software engineering project and served as practical experience in:

* Java Spring Boot development
* REST API integration
* Full-stack web development
* Software architecture and design patterns
* Domain-driven design concepts
* Database integration with JPA/Hibernate
* Asynchronous frontend communication using AJAX

---

## Author

Developed by Hannah Fitz

University project – winter semester 2025/26
