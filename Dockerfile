# STAGE 1: Dependencies (Caching)
# This stage downloads dependencies and is only invalidated if pom.xml changes.
FROM maven:3.9-eclipse-temurin-17 AS dependencies
# Use a Maven image with JDK (eclipse-temurin is lightweight and official)
WORKDIR /app
# 1. Copy only the configuration file (pom.xml)
COPY pom.xml .
# 2. Download dependencies in "offline" mode and store them in the local cache (~/.m2)
# This layer uses caching: if pom.xml doesn't change, Docker skips this step, saving time.
RUN mvn dependency:go-offline -B

# --------------------------------------------------------------------------

# STAGE 2: Application Build
# This stage compiles the application, reusing cached dependencies. It is only
# invalidated if the source code or pom.xml changes.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# 1. Copy the already downloaded dependencies from the previous stage’s cache
# This links the .m2 repository from 'dependencies' stage.
COPY --from=dependencies /root/.m2 /root/.m2
# 2. Copy the entire source code (this step invalidates the cache frequently)
COPY src src/
COPY pom.xml .
# 3. Compile the application, reusing the dependencies already present in ~/.m2
RUN mvn package -DskipTests

# --------------------------------------------------------------------------

# STAGE 3: Runtime (Lightweight Final Image)
# This is the final image, containing only Java (JRE) and the compiled artifact.
FROM eclipse-temurin:17-jre-alpine AS final
# Using a lightweight JRE image (e.g., -jre-alpine or -jre-slim) for minimal size.
WORKDIR /app
# 1. Copy the compiled artifact (the JAR file) from the 'build' stage
# Ensure the JAR name matches your final artifact:
COPY --from=build /app/target/BankingWebApp-0.0.1-SNAPSHOT.jar /app/app.jar
COPY src/main/resources/application.properties .
# 2. Command to start the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
