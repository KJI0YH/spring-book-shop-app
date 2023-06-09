FROM openjdk:17
WORKDIR /app
COPY . /app
RUN ./mvnw clean package -DskipTests
ADD target/*.jar MyBookShopApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "MyBookShopApp.jar"]