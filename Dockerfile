FROM openjdk:17
ADD target/MyBookShopApp-0.0.1-SNAPSHOT.jar MyBookShopApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "MyBookShopApp.jar"]