# Start from Maven base image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /build

# Copy only pom.xml first (to cache dependencies)
COPY pom.xml .

# Create empty src folder (to satisfy Maven validate)
RUN mkdir -p src/main/java

# Pre-download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY . .

# Package the application
RUN mvn clean package -DskipTests

# Use a lightweight image to run the app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /build/target/*.jar app.jar

# Copy wait script if needed
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

COPY runapp.sh .
RUN chmod +x runapp.sh

CMD ["./runapp.sh"]
