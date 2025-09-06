# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

@.ai-rule/

## Code Standards

**CRITICAL**: All code generation and modifications MUST strictly follow the Spring Code Standard defined in `.ai-rule/spring-code-standard.prompt.md`. This includes:
- Google Java Style Guide as baseline
- Tab indentation (4 spaces), 120 character line width
- Specific import ordering: java.* → jakarta.* → third-party → springframework.* → static imports
- K&R style braces, Clean Code principles, SOLID principles
- SLF4j logging (never System.out), meaningful naming, guard clauses

## Project Structure

This is a Spring Boot resource repository with two main executable projects:
- `projects/spring-mvc-template/` - Production-ready template with Spring Boot 3.2 + Java 21
- `projects/spring-mvc-example/` - Complete user management system example

Both use Maven with Spring Boot parent and follow identical layered architecture patterns.

## Build Commands

### Template Project
```bash
cd projects/spring-mvc-template
mvn spring-boot:run                  # Start application
mvn test                            # Run all tests  
mvn test -Dtest=ClassName           # Run single test class
mvn clean compile                   # Clean and compile
```

### Example Project  
```bash
cd projects/spring-mvc-example
mvn spring-boot:run                  # Start application
mvn test                            # Run all tests
mvn test -Dtest=ClassName           # Run single test class  
mvn clean compile                   # Clean and compile
```

## Architecture Overview

### Technology Stack
- Java 21, Spring Boot 3.2, Spring Security 6, Spring Data JPA
- MapStruct 1.5.5.Final for object mapping
- SpringDoc OpenAPI 3 for API documentation
- JUnit 5 + AssertJ for testing

### Layered Architecture
```
controller/ → service/ → repository/ → entity/
     ↓           ↓
   dto/ ←─── mapper/
```

### Key Components
- **Global Exception Handling**: Unified API responses via `GlobalExceptionHandler`
- **AOP Integration**: Logging, auditing, and performance aspects in `aspect/` package
- **MapStruct Mappers**: Automatic DTO-Entity conversion in `mapper/` package
- **Base Classes**: `BaseEntity` for common entity fields, `ApiResponse`/`PageResponse` for API responses

### Configuration Management
Priority: `application.yml` → `@ConfigurationProperties` (constructor binding) → `@Configuration` classes

## Development Notes

The template project serves as a starting point while the example project demonstrates complete business logic implementation. Both projects share the same architectural foundation with Spring Boot best practices, AOP features, and comprehensive testing setup.