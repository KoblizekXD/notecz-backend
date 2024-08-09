package lol.koblizek.notecz.api.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Saves a user to the database, the provided password should not
     * be handed as hashed, as it will be hashed automatically.
     * @param user User
     * @return User
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public boolean check(String username, String password) {
        return findUserByUsername(username).map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }
}
