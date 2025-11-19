# Build stage
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built application
COPY --from=build /workspace/app/target/quarkus-app /app

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "quarkus-run.jar"]