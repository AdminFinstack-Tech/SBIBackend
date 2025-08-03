# Arab Bank Trade Finance Backend API

A comprehensive Spring Boot backend API for Arab Bank's trade finance mobile application, providing secure RESTful services for financial operations, transaction management, and user authentication.

## Project Overview

This backend service provides a robust API layer for the Arab Bank mobile application with the following capabilities:
- **Secure Authentication & Authorization** with JWT tokens
- **Trade Finance Operations** (Import/Export LC, Guarantees, Collections)
- **Transaction Management** with multi-level approvals
- **User Management** with role-based access control
- **Document Processing** with OCR and ML capabilities
- **Notification Services** with Firebase integration
- **Analytics & Reporting** for business insights
- **Integration APIs** for external banking systems

## Architecture

```
src/
├── main/
│   ├── java/
│   │   └── com/csme/
│   │       ├── config/          # Spring configuration classes
│   │       ├── controller/      # REST API controllers
│   │       ├── service/         # Business logic services
│   │       ├── model/           # Entity models and DTOs
│   │       ├── repository/      # Data access layer
│   │       ├── security/        # Security configuration
│   │       ├── util/            # Utility classes
│   │       └── exception/       # Custom exceptions
│   └── resources/
│       ├── application.yaml     # Application configuration
│       ├── static/              # Static resources
│       └── templates/           # Email templates
└── test/                        # Unit and integration tests
```

## Features

### Core Banking Features
- **Authentication Services**: Multi-factor authentication with biometric support
- **Trade Finance Operations**: Import/Export Letters of Credit, Bank Guarantees
- **Payment Processing**: Domestic and international transfers
- **Account Management**: Balance inquiries, statements, account operations
- **Document Management**: Upload, processing, and storage of trade documents
- **Approval Workflows**: Multi-level authorization for high-value transactions

### Technical Features
- **JWT Authentication**: Secure token-based authentication with refresh tokens
- **Role-Based Access Control**: Granular permissions based on user roles
- **API Documentation**: OpenAPI/Swagger integration for API documentation
- **Database Integration**: PostgreSQL for production, H2 for testing
- **Caching Layer**: Redis for performance optimization
- **Message Queuing**: For asynchronous processing
- **File Processing**: OCR and document analysis capabilities
- **External Integrations**: Core banking system and payment gateways

## Technology Stack

### Core Technologies
- **Spring Boot**: 2.4.1 (Legacy), 3.2.0 (Modern)
- **Spring Security**: For authentication and authorization
- **Spring Data JPA**: Data access layer
- **PostgreSQL**: Primary database
- **Redis**: Caching and session management
- **Maven**: Build and dependency management

### Additional Libraries
- **JWT (JJWT)**: JSON Web Token implementation
- **OpenAPI 3**: API documentation
- **Lombok**: Code generation
- **MapStruct**: Bean mapping
- **Firebase Admin SDK**: Push notifications
- **Apache Commons**: Utility libraries
- **Tess4J**: OCR processing
- **Stanford CoreNLP**: Natural language processing
- **TensorFlow Java**: Machine learning models

## Prerequisites

### Required Software
- **Java JDK**: 8+ (Legacy backend) or 17+ (Modern backend)
- **Maven**: 3.6 or higher
- **PostgreSQL**: 12 or higher
- **Redis**: 6.0 or higher (optional, for caching)
- **Docker**: For containerized deployment

### Development Tools
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code
- **Database Client**: pgAdmin, DBeaver, or similar
- **API Testing**: Postman, Insomnia, or curl
- **Git**: Version control

## Installation & Setup

### 1. Clone and Navigate

```bash
git clone <repository-url>
cd banking1/backend
```

### 2. Database Setup

#### PostgreSQL Setup
```sql
-- Connect to PostgreSQL as superuser
CREATE DATABASE arab_bank_trade_finance;
CREATE USER banking_user WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE arab_bank_trade_finance TO banking_user;
```

#### Redis Setup (Optional)
```bash
# Install Redis (Ubuntu/Debian)
sudo apt update
sudo apt install redis-server

# Install Redis (macOS)
brew install redis
brew services start redis

# Install Redis (Windows)
# Download from https://redis.io/download
```

### 3. Environment Configuration

Create `application-local.yaml` in `src/main/resources/`:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/arab_bank_trade_finance
    username: banking_user
    password: your_secure_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

  redis:
    host: localhost
    port: 6379
    timeout: 2000ms
    password: # Set if Redis requires authentication

  mail:
    host: smtp.example.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

security:
  jwt:
    secret: ${JWT_SECRET:your-super-secret-jwt-key-that-should-be-at-least-256-bits}
    expiration: 86400000 # 24 hours in milliseconds
    refresh-expiration: 604800000 # 7 days in milliseconds

firebase:
  config:
    path: ${FIREBASE_CONFIG_PATH:./firebase-service-account.json}
    database-url: ${FIREBASE_DATABASE_URL}

logging:
  level:
    com.csme: DEBUG
    org.springframework.security: DEBUG
    org.springframework.web: DEBUG
  file:
    name: logs/application.log
  pattern:
    file: "%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n"
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

app:
  cors:
    allowed-origins: "http://localhost:3000,https://yourdomain.com"
    allowed-methods: "GET,POST,PUT,DELETE,OPTIONS"
    allowed-headers: "*"
    allow-credentials: true

  ocr:
    tessdata-path: ${TESSDATA_PATH:./tessdata}
    language: eng+ara

  ml:
    model-path: ${ML_MODEL_PATH:./models}
```

### 4. Environment Variables

Create `.env` file in the backend root:

```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=arab_bank_trade_finance
DB_USERNAME=banking_user
DB_PASSWORD=your_secure_password

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-that-should-be-at-least-256-bits
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Mail Configuration
MAIL_USERNAME=your-email@example.com
MAIL_PASSWORD=your-email-password
MAIL_HOST=smtp.example.com

# Firebase Configuration
FIREBASE_CONFIG_PATH=./firebase-service-account.json
FIREBASE_DATABASE_URL=https://your-project.firebaseio.com

# External API Configuration
CORE_BANKING_API_URL=https://core-banking-api.example.com
CORE_BANKING_API_KEY=your-api-key
PAYMENT_GATEWAY_URL=https://payment-gateway.example.com
PAYMENT_GATEWAY_KEY=your-payment-key

# OCR Configuration
TESSDATA_PATH=./tessdata
OCR_LANGUAGES=eng+ara

# ML Configuration
ML_MODEL_PATH=./models

# Environment
ENVIRONMENT=development
DEBUG_MODE=true
```

### 5. Firebase Setup

1. **Download Service Account Key**
   - Go to Firebase Console → Project Settings → Service Accounts
   - Generate new private key and download JSON file
   - Place as `firebase-service-account.json` in project root

2. **Configure Firebase**
   - Update `FIREBASE_CONFIG_PATH` in environment variables
   - Ensure Firebase project has Messaging and Analytics enabled

## Building & Running

### Development Mode

```bash
# Install dependencies and compile
mvn clean compile

# Run with development profile
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Run with specific port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Production Build

```bash
# Create production JAR
mvn clean package -Pprod

# Run production JAR
java -jar target/FinMobileApp-0.0.1-SNAPSHOT.war

# Run with external configuration
java -jar target/FinMobileApp-0.0.1-SNAPSHOT.war --spring.config.location=./application-prod.yaml
```

### Docker Deployment

```bash
# Build Docker image
docker build -t arab-bank-backend .

# Run with Docker
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=host.docker.internal \
  -e DB_PASSWORD=your_password \
  arab-bank-backend

# Run with Docker Compose
docker-compose up -d
```

## API Documentation

### Swagger UI
Once the application is running, access the API documentation at:
- **Development**: `http://localhost:8080/swagger-ui.html`
- **Production**: `https://your-domain.com/api/swagger-ui.html`

### OpenAPI Specification
- **JSON Format**: `http://localhost:8080/v3/api-docs`
- **YAML Format**: `http://localhost:8080/v3/api-docs.yaml`

### Authentication Endpoints

```bash
# User Registration
POST /api/auth/register
Content-Type: application/json
{
  "username": "user@example.com",
  "password": "SecurePassword123!",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890"
}

# User Login
POST /api/auth/login
Content-Type: application/json
{
  "username": "user@example.com",
  "password": "SecurePassword123!"
}

# Refresh Token
POST /api/auth/refresh
Authorization: Bearer <refresh-token>

# Logout
POST /api/auth/logout
Authorization: Bearer <access-token>
```

### Core API Endpoints

```bash
# Account Information
GET /api/accounts
Authorization: Bearer <token>

# Transaction History
GET /api/transactions?page=0&size=20
Authorization: Bearer <token>

# Initiate Transfer
POST /api/transfers
Authorization: Bearer <token>
Content-Type: application/json
{
  "fromAccount": "123456789",
  "toAccount": "987654321",
  "amount": 1000.00,
  "currency": "USD",
  "description": "Payment for services"
}

# Upload Document
POST /api/documents
Authorization: Bearer <token>
Content-Type: multipart/form-data
file: <document-file>
type: "LC_APPLICATION"
```

## Testing

### Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Integration Tests

```bash
# Run integration tests
mvn integration-test

# Run with test profile
mvn test -Dspring.profiles.active=test
```

### API Testing with Postman

1. **Import Collection**
   - Use the provided Postman collection in `/backend/FinMobileAPI.postman_collection.json`
   
2. **Set Environment Variables**
   - `base_url`: http://localhost:8080
   - `access_token`: Retrieved from login response

3. **Test Scenarios**
   - Authentication flow
   - CRUD operations
   - Error handling
   - Authorization checks

## Security Configuration

### Authentication & Authorization

```yaml
security:
  jwt:
    secret: your-256-bit-secret
    expiration: 86400000 # 24 hours
    header: Authorization
    prefix: "Bearer "
  
  cors:
    allowed-origins: 
      - "http://localhost:3000"
      - "https://yourdomain.com"
    allowed-methods: "GET,POST,PUT,DELETE,OPTIONS"
    allowed-headers: "*"
    allow-credentials: true

  rate-limiting:
    enabled: true
    requests-per-minute: 100
    burst-capacity: 200
```

### Database Security

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
  
  jpa:
    properties:
      hibernate:
        connection:
          provider_disables_autocommit: true
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
```

## Performance Optimization

### Caching Configuration

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 600000 # 10 minutes
      cache-null-values: false
      key-prefix: "arab_bank:"
      use-key-prefix: true
```

### Database Optimization

```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 25
        order_inserts: true
        order_updates: true
        connection:
          provider_disables_autocommit: true
        query:
          in_clause_parameter_padding: true
```

## Monitoring & Logging

### Application Monitoring

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  health:
    redis:
      enabled: true
    db:
      enabled: true
```

### Logging Configuration

```yaml
logging:
  level:
    com.csme: INFO
    org.springframework.security: WARN
    org.springframework.web: WARN
    org.hibernate.SQL: WARN
    org.hibernate.type.descriptor.sql.BasicBinder: WARN
  
  pattern:
    file: "%d{ISO8601} [%thread] %-5level [%X{traceId:-}] %logger{36} - %msg%n"
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level [%X{traceId:-}] %logger{36} - %msg%n"
  
  file:
    name: logs/arab-bank-backend.log
    max-size: 100MB
    max-history: 30
    total-size-cap: 1GB
```

## Deployment

### Production Deployment

1. **Prepare Production Configuration**
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
  
server:
  port: ${PORT:8080}
  ssl:
    enabled: true
    key-store: ${SSL_KEY_STORE}
    key-store-password: ${SSL_KEY_STORE_PASSWORD}
```

2. **Build Production JAR**
```bash
mvn clean package -Pprod -DskipTests
```

3. **Deploy to Server**
```bash
# Copy JAR to server
scp target/FinMobileApp-0.0.1-SNAPSHOT.war user@server:/opt/arab-bank/

# Create systemd service
sudo systemctl enable arab-bank-backend
sudo systemctl start arab-bank-backend
```

### Docker Production Deployment

```dockerfile
# Multi-stage build
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Pprod -DskipTests

FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=build /app/target/FinMobileApp-0.0.1-SNAPSHOT.war app.war
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.war"]
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: arab-bank-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: arab-bank-backend
  template:
    metadata:
      labels:
        app: arab-bank-backend
    spec:
      containers:
      - name: backend
        image: arab-bank-backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "k8s"
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

## Troubleshooting

### Common Issues

#### Database Connection Issues
```bash
# Check PostgreSQL connection
psql -h localhost -U banking_user -d arab_bank_trade_finance

# Verify database exists
\l

# Check user permissions
\du
```

#### Redis Connection Issues
```bash
# Test Redis connection
redis-cli ping

# Check Redis configuration
redis-cli config get "*"
```

#### JWT Token Issues
```bash
# Verify JWT secret is properly configured
echo $JWT_SECRET | wc -c  # Should be at least 32 characters

# Check token expiration settings
grep -r "jwt.expiration" src/main/resources/
```

### Performance Issues

#### Database Performance
```sql
-- Check slow queries
SELECT query, mean_time, calls 
FROM pg_stat_statements 
ORDER BY mean_time DESC LIMIT 10;

-- Monitor active connections
SELECT count(*) FROM pg_stat_activity;
```

#### Memory Issues
```bash
# Monitor JVM memory usage
jstat -gc <pid>

# Get heap dump for analysis
jcmd <pid> GC.run_finalization
jcmd <pid> VM.gc
```

### Debugging Tools

```bash
# Enable debug logging
java -jar app.war --logging.level.com.csme=DEBUG

# Remote debugging
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar app.war

# Profile application
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=profile.jfr -jar app.war
```

## API Reference

### Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for request parameters",
  "path": "/api/transfers",
  "errors": [
    {
      "field": "amount",
      "code": "INVALID_AMOUNT",
      "message": "Amount must be greater than zero"
    }
  ]
}
```

### Status Codes

- **200 OK**: Successful operation
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request parameters
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **409 Conflict**: Resource already exists
- **422 Unprocessable Entity**: Validation failed
- **500 Internal Server Error**: Server error

## Contributing

### Development Guidelines

1. **Code Style**
   - Follow Google Java Style Guide
   - Use Lombok for boilerplate code reduction
   - Write comprehensive JavaDoc for public APIs
   - Maintain test coverage above 80%

2. **Git Workflow**
   ```bash
   # Create feature branch
   git checkout -b feature/your-feature-name
   
   # Make changes and test
   mvn test
   
   # Commit with conventional commits
   git commit -m "feat(auth): add multi-factor authentication"
   
   # Push and create PR
   git push origin feature/your-feature-name
   ```

3. **Testing Requirements**
   - Unit tests for all service methods
   - Integration tests for API endpoints
   - Security tests for authentication flows
   - Performance tests for critical paths

## Support

### Documentation
- **API Documentation**: Available at `/swagger-ui.html`
- **Spring Boot Reference**: [spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- **Spring Security**: [spring.io/projects/spring-security](https://spring.io/projects/spring-security)

### Getting Help
- Review troubleshooting section above
- Check application logs in `logs/` directory
- Use health check endpoint: `/actuator/health`
- Contact development team for technical support

## License

This project is proprietary software developed for Arab Bank. All rights reserved.

## Changelog

### Version 2.4.1 (Current - Legacy Backend)
- Enhanced security with JWT refresh tokens
- Added OCR capabilities for document processing
- Integrated Firebase push notifications
- Improved error handling and validation
- Performance optimizations with Redis caching

### Version 3.2.0 (Modern Backend)
- Upgraded to Spring Boot 3.x with Java 17
- Enhanced OpenAPI documentation
- Improved security with latest Spring Security
- Added comprehensive health checks
- Modern dependency management and build configuration