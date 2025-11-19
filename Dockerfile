FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /workspace/app

# Install Maven
RUN apk add --no-cache maven

# Debug: Show current directory
RUN pwd
RUN ls -la

# Copy specific files first
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .

# Debug: Show files after copy
RUN ls -la
RUN ls -la src/

# Make mvnw executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /workspace/app/target/quarkus-app /app

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "quarkus-run.jar", "-Dquarkus.http.host=0.0.0.0"]