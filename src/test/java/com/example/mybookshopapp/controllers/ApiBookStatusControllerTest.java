package com.example.mybookshopapp.controllers;

import com.example.mybookshopapp.SpringBootApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ApiBookStatusControllerTest extends SpringBootApplicationTest {

    private final MockMvc mockMvc;

    @Autowired
    public ApiBookStatusControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @WithUserDetails("email@email.com")
    public void addBooksToCartTest() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"status\": \"CART\",\"booksIds\": [1,2]}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("email@email.com")
    public void deleteBooksFromCartTest() throws Exception {
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"status\": \"CART\",\"booksIds\": [1,2]}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/changeBookStatus")
                        .content("{\"status\": \"UNLINK\",\"booksIds\": [1,2]}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}