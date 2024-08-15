package lol.koblizek.notecz.api.auth;

import lol.koblizek.notecz.api.user.UserRepository;
import lol.koblizek.notecz.api.user.UserService;
import lol.koblizek.notecz.api.user.post.PostRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestAuthConfiguration {
    @Bean
    public UserService userService(PostRepository postRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserService(postRepository, userRepository, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
