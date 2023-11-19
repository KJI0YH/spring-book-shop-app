package com.example.mybookshopapp.security;

import com.example.mybookshopapp.SpringBootApplicationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/delete-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AuthUserControllerTest extends SpringBootApplicationTest {
    private final MockMvc mockMvc;

    @Test
    void loginByEmailTest() throws Exception {
        mockMvc.perform(formLogin("/signin").user("email@email.com").password("password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void loginByPhoneTest() throws Exception {
        mockMvc.perform(formLogin("/signin").user("+375336810213").password("password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void logoutTest() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin"));
    }

    @Test
//    @Sql(scripts = "classpath:/delete-users.sql")
    void registerNewUserTest() throws Exception {
        mockMvc.perform(post("/reg")
                        .param("name", "potato")
                        .param("phone", "+375123456789")
                        .param("email", "newemail@email.com")
                        .param("pass", "password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


}