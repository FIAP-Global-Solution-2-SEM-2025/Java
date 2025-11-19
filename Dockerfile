FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /workspace/app

# Install system Maven
RUN apk add --no-cache maven

# Copy application files from bpm-connect
COPY bpm-connect/pom.xml .
COPY bpm-connect/src ./src

# Build using system maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR file (not quarkus-app folder)
COPY --from=builder /workspace/app/target/skillfast-1.0.0-SNAPSHOT-runner.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar", "-Dquarkus.http.host=0.0.0.0"]