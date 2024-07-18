package lol.koblizek.notecz.api.user;

import jakarta.validation.ConstraintViolationException;
import lol.koblizek.notecz.api.user.User;
import lol.koblizek.notecz.api.user.UserRepository;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTests {

    static final String username = "John";
    static final String email = "john@example.com";
    static final String password = "Hello723";

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRepositoryAdding() {
        userRepository.saveAndFlush(User.builder()
                .username(username)
                .email(email)
                .password(password).build());
        assertThat(userRepository.findAll()).hasSize(1);
    }

    @Test
    void testInvalidUser() {
        User user = User.builder()
                .name("Ro") // Username is mandatory but name isn't!
                .email("ro@")
                .password("Ro")
                .build();
        assertThatThrownBy(() -> userRepository.saveAndFlush(user))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
