package lol.koblizek.notecz.api.user;

import jakarta.transaction.Transactional;
import lol.koblizek.notecz.api.user.post.Post;
import lol.koblizek.notecz.api.user.post.PostRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PostRepository postRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
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

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID must be set");
        }
        return userRepository.save(user);
    }

    @Transactional
    public boolean addPost(Long id, Post post) {
        return findUserById(id).map(user -> {
            post.setUser(user);
            List<Post> copy = user.getPosts();
            copy.add(post);
            user.setPosts(copy);
            postRepository.saveAndFlush(post);
            return true;
        }).orElse(false);
    }
}
