# Stage 1: Сборка
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/
COPY src ./src

RUN chmod +x ./gradlew

RUN ./gradlew build -x test

# Stage 2: Финальный образ
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/service-sync-gateway-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]