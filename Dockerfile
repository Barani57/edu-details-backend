# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/src/main/resources/serviceAccountKey.json /app/serviceAccountKey.json

# Environment variables
ENV PORT=8099
ENV SPRING_PROFILES_ACTIVE=prod
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/serviceAccountKey.json

# Expose the port
EXPOSE ${PORT}

# Run the application
CMD ["java", "-jar", "app.jar"]