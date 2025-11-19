FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /workspace/app

# Install Maven
RUN apk add --no-cache maven

# Copy the entire project
COPY . .

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built application from the builder stage
COPY --from=builder /workspace/app/target/quarkus-app /app

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "quarkus-run.jar", "-Dquarkus.http.host=0.0.0.0"]