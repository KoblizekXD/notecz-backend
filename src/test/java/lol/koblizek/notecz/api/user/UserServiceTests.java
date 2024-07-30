package lol.koblizek.notecz.api.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    User testUser = new User(
            1L,
            "xxxJohnxxx",
            "John Doe",
            "john.doe@example.com",
            "hashedPassword"
    );

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void testFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(userService.findUserById(1L)).hasValueSatisfying(usr -> {
            assertThat(usr.getUsername()).isEqualTo("xxxJohnxxx");
        });
        assertThat(userService.findUserById(2L)).isEmpty();
    }

    @Test
    void testFindUserByUsername() {
        when(userRepository.findByUsername("xxxJohnxxx")).thenReturn(Optional.of(testUser));
        assertThat(userService.findUserByUsername("xxxJohnxxx")).isNotEmpty();
        assertThat(userService.findUserByUsername("Johnxxx")).isEmpty();
    }

    @Test
    void testCreateUser() {
        User user = User.builder()
                .username("Johnxxx")
                .email("john.doe2@example.com").password("Password1").build();
        when(userRepository.save(user)).thenAnswer(inv -> inv.getArguments()[0]);
        when(passwordEncoder.encode("Password1")).thenReturn("hashedPassword");

        assertThat(userService.createUser(user)).isNotNull();
    }

    @Test
    void testUserCheck() {
        when(userRepository.findByUsername("xxxJohnxxx")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("Password123", "hashedPassword")).thenReturn(true);

        assertThat(userService.check("xxxJohnxxx", "Password123")).isTrue();
        assertThat(userService.check("xxxJohnxxx", "Password1")).isFalse();
    }
}
