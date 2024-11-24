FROM gradle:8.10.2-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle :user-service:build -x test

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/user-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]