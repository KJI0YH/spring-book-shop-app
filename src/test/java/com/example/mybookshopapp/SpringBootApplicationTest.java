package com.example.mybookshopapp;

import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml, classpath:application-dev-test.yml"})
public class SpringBootApplicationTest {

    private static final String DATABASE_NAME = "book-shop-db-test";

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(DATABASE_NAME);

    @DynamicPropertySource
    public static void datasourceProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @AfterAll
    public static void tearDownAll(){
        if (postgreSQLContainer != null && postgreSQLContainer.isRunning()) {
            postgreSQLContainer.stop();
        }
    }
}
