package lol.koblizek.notecz.api.auth;

import lol.koblizek.notecz.api.user.User;
import lol.koblizek.notecz.api.user.UserService;
import lol.koblizek.notecz.util.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@TestPropertySource(value = "classpath:application-test.properties")
class AuthServiceTests {

    @Mock
    UserService userService;

    @InjectMocks
    AuthService authService;

    @Value("${notecz.auth.jwt.expire}")
    Long validFor;
    @Value("${notecz.auth.jwt.secret}")
    String secret;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService, validFor, secret);
    }

    @Test
    void testRegisterUserSuccessfully() {
        when(userService.existsByEmail("non.existing@example.com")).thenReturn(false);
        when(userService.createUser(any())).thenReturn(new User("username", "non.existing@example.com", "Password1"));

        assertThat(authService.register("username", "non.existing@example.com", "Password1"))
                .isNotNull();
    }

    @Test
    void testRegisterUserUnsuccessfully() {
        when(userService.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register("username", "existing@example.com", "Password1"))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void testConstructToken() {
        User user = new User("username", "non.existing@example.com", "Password1");
        JwtToken token = authService.constructToken(user);

        assertThat(token).isNotNull();
        assertThat(token.getExpiration()).isNotNull();
        assertThat(token.getAllClaims()).isNotNull();
        assertThat(token.isTokenExpired()).isFalse();
    }

    @Test
    void testCreateExisting() {
        User user = new User("username", "non.existing@example.com", "Password1");
        JwtToken token1 = authService.constructToken(user);
        JwtToken token2 = authService.createExisting(token1.toString());

        assertThat(token1.token()).isEqualTo(token2.token());
    }
}
