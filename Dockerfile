FROM openjdk:17
RUN ./mwnw clean package -DskipTests
ADD target/*.jar MyBookShopApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "MyBookShopApp.jar"]