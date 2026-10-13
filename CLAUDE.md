# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

### Important Notes
Do not modify and generate code until I ask you to.

## Project Overview

This is a Spring Boot 4.1.1 application running on Java 25, designed as a loan model mapper service. The project is configured with:

- Spring Web MVC for RESTful endpoints
- Spring Boot Actuator with Prometheus metrics
- GraalVM native image support
- Lombok for reducing boilerplate code
- Spring Boot DevTools for development

## Build and Run Commands

### Standard Development

```bash
# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=LoanModelMapperApplicationTests

# Build the project
./mvnw clean package

# Clean build artifacts
./mvnw clean
```

### GraalVM Native Image

```bash
# Build native image using Spring Boot buildpacks (requires Docker)
./mvnw spring-boot:build-image -Pnative

# Run the native container
docker run --rm -p 8080:8080 loan-model-mapper:0.0.1-SNAPSHOT

# Build native executable directly (requires GraalVM 25+)
./mvnw native:compile -Pnative

# Run native executable
./target/loan-model-mapper

# Run tests in native image
./mvnw test -PnativeTest
```

## Architecture

This is a Spring Boot application with the main class at `src/main/java/com/adme/learning/loan/model/mapper/LoanModelMapperApplication.java`. The project follows standard Spring Boot conventions:

- **Package structure**: `com.adme.learning.loan.model.mapper`
- **Actuator endpoints**: Available for health checks and metrics (Prometheus format)
- **Lombok**: Configured with annotation processor for both compile and test-compile phases

The application is designed for cloud-native deployment with actuator monitoring and Prometheus metrics support.