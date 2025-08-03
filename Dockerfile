# Arab Bank Trade Finance Backend - Dockerfile
# Multi-stage build for production-ready Spring Boot application

# Build stage
FROM maven:3.8-openjdk-8-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Copy additional resources
COPY MOBILE ./MOBILE
COPY INT ./INT

# Build the application
ARG MAVEN_OPTS="-Xmx1024m"
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM openjdk:8-jre-alpine

# Install additional packages needed for banking operations
RUN apk add --no-cache \
    curl \
    wget \
    tzdata \
    fontconfig \
    ttf-dejavu \
    bash

# Set timezone
ENV TZ=Asia/Dubai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Create app user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Create necessary directories
RUN mkdir -p /app/logs /app/uploads /app/temp /app/tessdata /app/models && \
    chown -R appuser:appgroup /app

# Copy the built JAR file
COPY --from=build /app/target/FinMobileApp-*.war /app/app.war

# Copy additional configuration files
COPY --chown=appuser:appgroup src/main/resources/application.yaml /app/config/
COPY --chown=appuser:appgroup docker/backend-entrypoint.sh /app/entrypoint.sh

# Make the entrypoint script executable
RUN chmod +x /app/entrypoint.sh

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for production
ENV JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker"

# Run the application
ENTRYPOINT ["./entrypoint.sh"]
CMD ["java", "-jar", "app.war"]