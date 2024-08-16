package lol.koblizek.notecz.api.user;

import lol.koblizek.notecz.api.user.post.Post;
import lol.koblizek.notecz.api.user.post.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Long id) {
        return userService.findUserById(id).map(user -> ResponseEntity.ok(user.getPosts()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/posts")
    @PreAuthorize("hasAuthority('CREATE_POSTS')")
    public ResponseEntity<Post> createPost(@RequestBody Post post, Authentication manager) {
        User user = (User) manager.getPrincipal();
        post.setUser(userService.findUserById(user.getId()).get());
        return new ResponseEntity<>(postRepository.save(post), HttpStatus.CREATED);
    }
}
