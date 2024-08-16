package lol.koblizek.notecz.api.user;

import lol.koblizek.notecz.api.user.post.Post;
import lol.koblizek.notecz.api.user.post.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PostRepository postRepository;

    public UserController(UserService userService, PostRepository postRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findUserById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<Set<Post>> getUserPosts(@PathVariable Long id) {
        return userService.findUserById(id).map(user -> ResponseEntity.ok(user.getPosts()))
                .orElse(ResponseEntity.notFound().build());
    }
}
