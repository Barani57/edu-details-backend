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

# Environment variables
ENV PORT=8099
ENV SPRING_PROFILES_ACTIVE=prod

# Expose the port
EXPOSE ${PORT}

# Run the application
CMD ["java", "-jar", "app.jar"]