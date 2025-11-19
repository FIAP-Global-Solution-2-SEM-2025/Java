FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /workspace/app

# Copy Maven wrapper from ROOT
COPY mvnw .
# Copy .mvn folder from bpm-connect
COPY bpm-connect/.mvn .mvn

# Copy application files from bpm-connect
COPY bpm-connect/pom.xml .
COPY bpm-connect/src ./src

# Make mvnw executable and build
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/app/target/quarkus-app /app

EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar", "-Dquarkus.http.host=0.0.0.0"]