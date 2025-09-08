# ---- Build stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Make mvnw executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw -B -DskipTests clean package

# ---- Runtime stage ----
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

CMD ["sh", "-c", "java -jar app.jar --server.port=$PORT --server.address=0.0.0.0"]
