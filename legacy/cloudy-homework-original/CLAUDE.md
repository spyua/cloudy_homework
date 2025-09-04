# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a multi-module Maven project for a cloud-based file management system with the following modules:

- **cloudy-account**: User authentication and account management service (Spring Boot)
- **cloudy-files**: File upload/download and processing service (Spring Boot) 
- **cloudy-security**: Shared security components with JWT authentication and Google Cloud KMS encryption
- **cloudy-event**: Google Cloud Functions for image compression and event processing

## Technology Stack

- Java 17 (cloudy-account, cloudy-files, cloudy-security) / Java 11 (cloudy-event)
- Spring Boot 3.1.0
- Spring Security with JWT authentication
- Google Cloud Platform services (KMS, Secret Manager, Cloud SQL, Cloud Storage)
- PostgreSQL database
- Maven multi-module build

## Common Development Commands

### Building and Running

```bash
# Build entire project
mvn clean install

# Run specific module
cd cloudy-account
mvn spring-boot:run

cd cloudy-files
mvn spring-boot:run

# Build Cloud Function
cd cloudy-event
mvn clean package
```

### Testing

```bash
# Run tests for all modules
mvn test

# Run tests for specific module
cd [module-name]
mvn test
```

## Architecture

### Security Architecture
- JWT-based authentication implemented in `cloudy-security` module
- Custom password encoder using Google Cloud KMS (`CloudKMSPasswordEncoder`)
- Shared security configurations and filters across modules
- Cross-module component scanning with base package `com.ck.*`

### Database Access
- JPA/Hibernate with PostgreSQL
- Google Cloud SQL integration
- Database credentials managed via Google Secret Manager
- Cross-module repository scanning enabled

### File Processing Flow
1. Files uploaded via `cloudy-files` service
2. Original files stored in Google Cloud Storage bucket `image_original`
3. Cloud Function (`cloudy-event`) triggered on storage events for image compression
4. Processed files metadata tracked in database

### Module Dependencies
- `cloudy-account` depends on `cloudy-security`
- `cloudy-files` depends on `cloudy-security`
- `cloudy-event` is standalone Google Cloud Function

## Configuration

### Environment Variables
- Google Cloud Project ID: `my-project-1507703752854`
- Database secrets managed through Google Secret Manager:
  - `sm://security_db_acc` and `sm://security_db_pass` (cloudy-account)
  - `sm://process_db_acc` and `sm://process_db_pass` (cloudy-files)

### Key Configuration Properties
- KMS key ID: `login/accountLogin3`
- Cloud SQL instance: `my-project-1507703752854:asia-east1:cloudy-homework`
- File upload limits: 10MB max file/request size
- Storage bucket: `image_original`

## Important Notes

- All modules use cross-package component scanning (`@ComponentScan(basePackages = {"com.ck.*"})`)
- Security is centralized in the `cloudy-security` module
- Authentication endpoints `/login` and `/register` are public
- File operations require JWT authentication
- Cloud Function uses Java 11 while other modules use Java 17