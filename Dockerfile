# ---- Build stage ----
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests clean package

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT --server.address=0.0.0.0"]
