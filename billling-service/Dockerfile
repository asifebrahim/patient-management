# --- Stage 1: Build ---
FROM maven:3.9.9-eclipse-temurin-23 AS builder

# Set working directory
WORKDIR /app

# Copy POM file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime ---
FROM openjdk:23-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/target/billling-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on
EXPOSE 8091
EXPOSE 9001

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
