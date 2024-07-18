package lol.koblizek.notecz.api.user;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTests {

    static final String username = "John";
    static final String email = "john@example.com";
    static final String password = "Hello723";

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRepositoryAdding() {
        userRepository.save(User.builder()
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
