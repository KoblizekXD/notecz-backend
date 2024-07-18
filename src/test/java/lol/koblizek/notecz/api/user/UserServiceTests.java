package lol.koblizek.notecz.api.user;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(
                1L,
                "xxxJohnxxx",
                "John Doe",
                "john.doe@example.com",
                "Password123"
        )));
        assertThat(userService.getUserById(1L)).isNotEmpty();
        assertThat(userService.getUserById(2L)).isEmpty();
    }

    @Test
    void testSave() {
        User userWrong = User.builder()
                .username("Johnxxx")
                .name("John Doe")
                .email("john.doe").password("Password1").build();
        User userCorrect = User.builder()
                .username("Johnxxx")
                .email("john.doe2@example.com").password("Password1").build();
        assertThatThrownBy(() -> userService.save(userWrong))
                .isInstanceOf(ConstraintViolationException.class);
        assertThat(userService.save(userCorrect)).isNotNull();
    }
}
