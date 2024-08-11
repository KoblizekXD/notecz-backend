package lol.koblizek.notecz.api.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lol.koblizek.notecz.api.auth.data.LoginResponse;
import lol.koblizek.notecz.api.user.User;
import lol.koblizek.notecz.api.user.UserLoginDto;
import lol.koblizek.notecz.api.user.UserRegistrationDto;
import lol.koblizek.notecz.api.user.UserService;
import lol.koblizek.notecz.util.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthService {

    private final UserService userService;

    private final Long validFor;
    private final String secret;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       @Value("${notecz.auth.jwt.expire}") Long validFor,
                       @Value("${notecz.auth.jwt.secret}") String secret) {
        this.userService = userService;
        this.validFor = validFor;
        this.secret = secret;
        this.passwordEncoder = passwordEncoder;
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(Base64.getMimeDecoder().decode(secret));
    }

    public LoginResponse register(String username, String email, String password) {
        if (userService.existsByEmail(email))
            throw new UserAlreadyExistsException(email);

        User user = userService.createUser(new User(username, email, password));
        JwtToken token = constructToken(user);
        return new LoginResponse(
                token.toString(),
                token.getExpiration(),
                user.getAuthorities()
        );
    }

    public LoginResponse register(UserRegistrationDto userRegistrationDto) {
        return register(userRegistrationDto.username(), userRegistrationDto.email(), userRegistrationDto.password());
    }

    public LoginResponse login(UserLoginDto userLoginDto) {
        return userService.findUserByEmail(userLoginDto.email())
                .map(user -> {
                    if (passwordEncoder.matches(userLoginDto.password(), user.getPassword())) {
                        JwtToken token = constructToken(user);
                        return new LoginResponse(
                                token.toString(),
                                token.getExpiration(),
                                user.getAuthorities()
                        );
                    } else throw new BadCredentialsException(userLoginDto.email());
                }).orElseThrow(() -> new BadCredentialsException(userLoginDto.email()));
    }

    /**
     * Generates JWT token for given user
     * @param user User to generate token for
     * @return JWT token
     */
    public JwtToken constructToken(User user) {
        return new JwtToken(Jwts.builder()
                .issuer("notecz")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(validFor)))
                .subject(user.getEmail())
                .signWith(getSignKey())
                .compact(), getSignKey());
    }

    public JwtToken createExisting(String token) {
        return new JwtToken(token, getSignKey());
    }
}
