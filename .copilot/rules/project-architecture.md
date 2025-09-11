# Project Architecture

This is a Java application with Spring Boot, Thymeleaf built with Maven.

## Clean DDD principles

- Following closely DDD, Clean Architecture, and Hexagonal Architecture principles
    - Models are in `src/main/java/com/github/cargoclean/core/model`
    - Use cases are in `src/main/java/com/github/cargoclean/core/usecase`
    - Output ports are in `src/main/java/com/github/cargoclean/core/port`
    - Adapters are `src/main/java/com/github/cargoclean/infrastructure/adapter`
