package lol.koblizek.notecz.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lol.koblizek.notecz.api.auth.data.LoginResponse;
import lol.koblizek.notecz.api.user.post.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    void testGetPublicUser() throws Exception {
        userService.createUser(new User("John", "john@example.com", "Password1"));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.username").value("John"))
                .andExpect(jsonPath("$.permissions").doesNotExist());
        mockMvc.perform(get("/api/users/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserPosts() throws Exception {
        User user = userService.createUser(new User("John", "john@example.com", "Password1"));
        assertThat(userService.addPost(user.getId(), new Post("Title", "Content Content Content")))
                .isTrue();
        mockMvc.perform(get("/api/users/" + user.getId() + "/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$[0].content").value("Content"));
    }

    @Test
    void testCreatePost() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        LoginResponse resp = objectMapper.reader().readValue(mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserRegistrationDto("Test", "test@email.com", "Password1"))))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString().getBytes(), LoginResponse.class);
        assertThat(resp).isNotNull();
        mockMvc.perform(post("/api/users/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + resp.token())
                        .content(objectMapper.writeValueAsString(new Post("Title", "Content Content Content")))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.content").value("Content Content Content"));
    }
}
