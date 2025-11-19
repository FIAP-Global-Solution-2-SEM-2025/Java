FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /workspace/app

# Copy everything from bpm-connect
COPY bpm-connect/. .

# Make mvnw executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built application
COPY --from=build /workspace/app/target/quarkus-app /app

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "quarkus-run.jar", "-Dquarkus.http.host=0.0.0.0"]