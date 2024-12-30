# Build stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /mob_backend
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn ./.mvn
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /mob_backend
COPY --from=builder /mob_backend/target/*.jar app.jar
COPY src/main/resources/serviceAccountKey.json serviceAccountKey.json

# Environment variables
ENV PORT=8099
ENV SPRING_PROFILES_ACTIVE=prod
ENV GOOGLE_APPLICATION_CREDENTIALS=/mob_backend/serviceAccountKey.json

# Expose the port
EXPOSE ${PORT}

# Run the application
CMD ["java", "-jar", "app.jar"]