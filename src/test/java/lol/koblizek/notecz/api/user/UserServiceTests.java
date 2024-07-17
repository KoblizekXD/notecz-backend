package lol.koblizek.notecz.api.user;

import jakarta.validation.ConstraintViolationException;
import lol.koblizek.notecz.api.user.User;
import lol.koblizek.notecz.api.user.UserRepository;
import lol.koblizek.notecz.api.user.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceTests {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(
                1L,
                "xxxJohnxxx",
                "John Doe",
                "john.doe@example.com",
                "Password123"
        )));
    }

    @Test
    void testGetUserById() {
        assertThat(userService.getUserById(1L)).isNotEmpty();
        assertThat(userService.getUserById(2L)).isEmpty();
    }

    @Test
    void testSave() {
        User user = User.builder()
                .username("Johnxxx")
                .name("John Doe")
                .email("dwad").password("Password1").build();
        assertThat(userService.save(user)).isPresent();
    }
}
