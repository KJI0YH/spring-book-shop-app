package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.SpringBootApplicationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApiControllerTest extends SpringBootApplicationTest {
    private final MockMvc mockMvc;

    @Test
    void getRecentBooksPage() throws Exception {
        mockMvc.perform(get("/api/books/recent")
                        .param("offset", "0")
                        .param("limit", "3")
                        .param("from", "")
                        .param("to", "")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void getRecommendedBooksPage() throws Exception {
        mockMvc.perform(get("/api/books/recommended")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void getPopularBooksPage() throws Exception {
        mockMvc.perform(get("/api/books/popular")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    @WithUserDetails("email@email.com")
    void getBooksViewedPage() throws Exception {
        mockMvc.perform(get("/api/books/viewed")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void getBooksByTagPage() throws Exception {
        mockMvc.perform(get("/api/books/tag/1")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void getBooksByGenreIdPage() throws Exception {
        mockMvc.perform(get("/api/books/genre/1")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void getBooksByAuthorIdPage() throws Exception {
        mockMvc.perform(get("/api/books/author/1")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    @WithUserDetails("email@email.com")
    void getBooksMyPage() throws Exception {
        mockMvc.perform(get("/api/books/my")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(1)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void getBooksMyPageUnauthorized() throws Exception {
        mockMvc.perform(get("/api/books/my")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void getBooksMyArchivePage() throws Exception {
        mockMvc.perform(get("/api/books/my/archive")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(1)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void getBooksMyArchivePageUnauthorized() throws Exception {
        mockMvc.perform(get("/api/books/my/archive")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusKept() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[5],\"status\":\"KEPT\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusCart() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[5],\"status\":\"CART\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusKeptToCart() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[1],\"status\":\"CART\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusCartToKept() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[2],\"status\":\"KEPT\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusToPaid() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[5],\"status\":\"PAID\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusToArchived() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[5],\"status\":\"ARCHIVED\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isPaymentRequired())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusPaidToArchived() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[3],\"status\":\"ARCHIVED\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusArchivedToPaid() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[4],\"status\":\"PAID\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusNotPaidUnlink() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[1],\"status\":\"UNLINK\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusPaidUnlink() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[3],\"status\":\"UNLINK\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleChangeBookStatusArchivedUnlink() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"booksIds\":[4],\"status\":\"UNLINK\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleRateBook() throws Exception {
        mockMvc.perform(post("/api/rateBook")
                        .content("{\"bookId\":\"1\",\"value\":\"5\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    void handleRateBookUnauthorized() throws Exception {
        mockMvc.perform(post("/api/rateBook")
                        .content("{\"bookId\":\"1\",\"value\":\"5\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleRateBookBadValue() throws Exception {
        mockMvc.perform(post("/api/rateBook")
                        .content("{\"bookId\":\"1\",\"value\":\"6\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleRateBookBadBookId() throws Exception {
        mockMvc.perform(post("/api/rateBook")
                        .content("{\"bookId\":\"1000\",\"value\":\"5\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleBookReview() throws Exception {
        mockMvc.perform(post("/api/bookReview")
                        .content("{\"bookId\":\"1\",\"text\":\"review text\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    void handleBookReviewUnauthorized() throws Exception {
        mockMvc.perform(post("/api/bookReview")
                        .content("{\"bookId\":\"1\",\"text\":\"review text\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleBookReviewBadReview() throws Exception {
        mockMvc.perform(post("/api/bookReview")
                        .content("{\"bookId\":\"1\",\"text\":\"\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleBookReviewBadBookId() throws Exception {
        mockMvc.perform(post("/api/bookReview")
                        .content("{\"bookId\":\"1000\",\"text\":\"review text\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleRateBookReview() throws Exception {
        mockMvc.perform(post("/api/rateBookReview")
                        .content("{\"reviewId\":\"100\",\"value\":\"1\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    void handleRateBookReviewUnauthorized() throws Exception {
        mockMvc.perform(post("/api/rateBookReview")
                        .content("{\"reviewId\":\"100\",\"value\":\"1\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleRateBookReviewBadReviewId() throws Exception {
        mockMvc.perform(post("/api/rateBookReview")
                        .content("{\"reviewId\":\"1000\",\"value\":\"1\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleRateBookReviewBadValue() throws Exception {
        mockMvc.perform(post("/api/rateBookReview")
                        .content("{\"reviewId\":\"100\",\"value\":\"0\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    void getSearchResultPage() throws Exception {
        mockMvc.perform(get("/api/search/Title")
                        .param("offset", "0")
                        .param("limit", "4")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(4)))
                .andExpect(jsonPath("$.books", not(empty())));
    }

    @Test
    void handleContactMessage() throws Exception {
        mockMvc.perform(post("/api/contacts/message")
                        .param("name", "name-1")
                        .param("mail", "email@email.com")
                        .param("title", "title")
                        .param("message", "message")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                )
                .andDo(print())
                .andExpect(status().isSeeOther())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    @WithUserDetails("email@email.com")
    void getTransactionsPage() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("offset", "0")
                        .param("limit", "3")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(3)))
                .andExpect(jsonPath("$.transactions", not(empty())));
    }

    @Test
    void getTransactionsPageUnauthorized() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .param("offset", "0")
                        .param("limit", "3")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    void handlePayment() throws Exception {
        mockMvc.perform(post("/api/payment")
                        .content("{\"hash\":\"hash1\",\"sum\":\"10000\",\"time\": \"1698160238\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(true)))
                .andExpect(jsonPath("$.message", not(empty())));
    }

    @Test
    void handlePaymentBadSum() throws Exception {
        mockMvc.perform(post("/api/payment")
                        .content("{\"hash\":\"hash1\",\"sum\":\"one million\",\"time\": \"1698160238\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }

    @Test
    void handlePaymentBadHash() throws Exception {
        mockMvc.perform(post("/api/payment")
                        .content("{\"hash\":\"no exists\",\"sum\":\"10000\",\"time\": \"1698160238\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result", is(false)));
    }
}