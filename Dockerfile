FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /workspace/app

# Install system Maven
RUN apk add --no-cache maven

# Copy application files from bpm-connect
COPY bpm-connect/pom.xml .
COPY bpm-connect/src ./src

# Build using system maven (no mvnw needed)
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/app/target/quarkus-app /app

EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar", "-Dquarkus.http.host=0.0.0.0"]