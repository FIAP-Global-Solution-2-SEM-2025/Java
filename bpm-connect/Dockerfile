FROM eclipse-temurin:21-jdk-alpine

WORKDIR /workspace/app

# Copy everything from bpm-connect
COPY bpm-connect .

# Build the application
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/app/target/quarkus-app /app

EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar", "-Dquarkus.http.host=0.0.0.0"]