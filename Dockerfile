# syntax=docker/dockerfile:1

FROM gradle:7.4-jdk18-alpine

WORKDIR /app

COPY build.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew jar

CMD ["./gradlew", "run"]
