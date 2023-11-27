# Stage 1: Build Stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Run Stage
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/*.jar MyBookShopApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "MyBookShopApp.jar"]
