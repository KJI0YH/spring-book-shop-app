package com.example.mybookshopapp.services;

import com.example.mybookshopapp.SpringBootApplicationTest;
import com.example.mybookshopapp.data.BookReviewEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;

@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookReviewServiceTest extends SpringBootApplicationTest {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    BookReviewServiceTest(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Test
    void BookReviewRatingTest() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        TypedQuery<BookReviewEntity> reviewQuery = entityManager.createQuery(
                "SELECT r FROM BookReviewEntity r LEFT JOIN FETCH r.reviewLikeList WHERE r.book.id = 1", BookReviewEntity.class);
        List<BookReviewEntity> reviews = reviewQuery.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        Assertions.assertNotNull(reviews);
        Assertions.assertFalse(reviews.isEmpty());
        Collections.sort(reviews);

        long previousValue = reviews.get(0).getPopularityValue();
        for (BookReviewEntity bookReview : reviews) {
            Assertions.assertTrue(previousValue <= bookReview.getPopularityValue());
            previousValue = bookReview.getPopularityValue();
        }

        Assertions.assertEquals(-2, reviews.get(0).getPopularityValue());
        Assertions.assertEquals(0, reviews.get(1).getPopularityValue());
        Assertions.assertEquals(2, reviews.get(2).getPopularityValue());
    }
}