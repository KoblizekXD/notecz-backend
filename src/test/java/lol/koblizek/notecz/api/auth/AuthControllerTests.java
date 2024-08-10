package lol.koblizek.notecz.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lol.koblizek.notecz.api.user.User;
import lol.koblizek.notecz.api.user.UserLoginDto;
import lol.koblizek.notecz.api.user.UserRegistrationDto;
import lol.koblizek.notecz.api.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    void testRegister() {
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"Test\",\"email\":\"\"}"))
                    .andExpect(status().isBadRequest());
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UserRegistrationDto("Test", "test@email.com", "Password1"))))
                    .andExpect(status().is(201));
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UserRegistrationDto("Test", "test@email.com", "Password1"))))
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    void testLoginSuccessful() {
        ObjectMapper objectMapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            userService.createUser(new User("Test", "existing@email.com", "Password1"));
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UserLoginDto("existing@email.com", "Password1"))))
                    .andExpect(status().isOk());
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UserLoginDto("non.existing@email.com", "Password1"))))
                    .andExpect(status().isUnauthorized());
        });
    }
}
