package lol.koblizek.notecz.api.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lol.koblizek.notecz.api.auth.data.LoginResponse;
import lol.koblizek.notecz.api.user.User;
import lol.koblizek.notecz.api.user.UserDto;
import lol.koblizek.notecz.api.user.UserService;
import lol.koblizek.notecz.util.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
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

    public AuthService(UserService userService,
                       @Value("${notecz.auth.jwt.expire}") Long validFor,
                       @Value("${notecz.auth.jwt.secret}") String secret) {
        this.userService = userService;
        this.validFor = validFor;
        this.secret = secret;
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
                Permission.CREATE_POSTS
        );
    }

    public LoginResponse register(UserDto userDto) {
        return register(userDto.username(), userDto.email(), userDto.password());
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
