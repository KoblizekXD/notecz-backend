package lol.koblizek.notecz.api.auth;

import lol.koblizek.notecz.api.user.User;
import lol.koblizek.notecz.api.user.UserLoginDto;
import lol.koblizek.notecz.api.user.UserService;
import lol.koblizek.notecz.util.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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

    @Mock
    PasswordEncoder passwordEncoder;

    @Value("${notecz.auth.jwt.expire}")
    Long validFor;
    @Value("${notecz.auth.jwt.secret}")
    String secret;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService, passwordEncoder, validFor, secret);
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

    @Test
    void testLoginUserSuccessfully() {
        when(userService.findUserByEmail("existing@email.com")).thenReturn(Optional.of(new User("username", "existing@email.com", "Password1")));
        when(passwordEncoder.matches("Password1", "Password1")).thenReturn(true);

        assertThat(authService.login(new UserLoginDto("existing@email.com", "Password1"))).isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    void testLoginUserUnsuccessfully() {
        when(userService.findUserByEmail("non.existing@email.com")).thenReturn(Optional.empty());
        when(userService.findUserByEmail("wrong.password@email.com")).thenReturn(Optional.of(new User("username", "wrong.password@email.com", "CorrectPassword")));
        assertThatThrownBy(() -> authService.login(new UserLoginDto("non.existing@email.com", "Password1")))
                .isInstanceOf(BadCredentialsException.class);
        assertThatThrownBy(() -> authService.login(new UserLoginDto("wrong.password@email.com", "Password1")))
                .isInstanceOf(BadCredentialsException.class);
    }
}
