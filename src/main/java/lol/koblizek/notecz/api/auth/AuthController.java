package lol.koblizek.notecz.api.auth;

import jakarta.validation.Valid;
import lol.koblizek.notecz.api.auth.data.LoginResponse;
import lol.koblizek.notecz.api.user.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> register(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(authService.register(userDto), HttpStatus.CREATED);
    }
}
