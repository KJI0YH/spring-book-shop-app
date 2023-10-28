package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.SpringBootApplicationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookControllerTest extends SpringBootApplicationTest {
    private final MockMvc mockMvc;

    @Test
    @WithUserDetails("email@email.com")
    void handleDownloadBookFile() throws Exception {
        mockMvc.perform(get("/books/download/hash3"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void handleDownloadBookFileUnauthorized() throws Exception {
        mockMvc.perform(get("/books/download/hash3"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleDownloadBookFileNotPaid() throws Exception {
        mockMvc.perform(get("/books/download/hash1"))
                .andDo(print())
                .andExpect(status().isPaymentRequired());
    }

    @Test
    @WithUserDetails("email@email.com")
    void handleDownloadBookFileBadHash() throws Exception {
        mockMvc.perform(get("/books/download/notexists"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}