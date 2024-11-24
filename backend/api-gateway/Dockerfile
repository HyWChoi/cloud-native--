FROM gradle:8.10.2-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle :api-gateway:build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/api-gateway/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]