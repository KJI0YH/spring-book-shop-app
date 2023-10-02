package com.example.mybookshopapp.security;

import com.example.mybookshopapp.SpringBootApplicationTest;
import com.example.mybookshopapp.dto.RegistrationForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
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
class AuthUserControllerTests extends SpringBootApplicationTest {

    private final MockMvc mockMvc;

    @Autowired
    public AuthUserControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void loginByEmailTest() throws Exception{
        mockMvc.perform(formLogin("/signin").user("email@email.com").password("password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Sql(scripts = "classpath:/delete-data.sql")
    public void registerNewUserTest() throws Exception{

        RegistrationForm regForm = getTestRegistrationForm();

        mockMvc.perform(post("/reg")
                        .param("name", regForm.getName())
                        .param("phone", regForm.getPhone())
                        .param("email", regForm.getEmail())
                        .param("pass", regForm.getPass())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("email@email.com")
    public void logoutTest() throws Exception {
        mockMvc.perform(logout("/logout"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signin"));
    }

    private RegistrationForm getTestRegistrationForm() {
        RegistrationForm regForm = new RegistrationForm();
        regForm.setName("TestName");
        regForm.setPhone("+712345678900");
        regForm.setEmail("test@test.com");
        regForm.setPass("test");
        return regForm;
    }
}