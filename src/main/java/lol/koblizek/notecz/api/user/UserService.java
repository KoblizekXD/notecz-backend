package lol.koblizek.notecz.api.user;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> save(User user) {
        try {
            return Optional.of(userRepository.saveAndFlush(user));
        } catch (ConstraintViolationException e) {
            LOGGER.warn("User validation failed: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
