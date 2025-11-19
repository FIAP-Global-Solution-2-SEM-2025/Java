FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/app/target/quarkus-app /app
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]
