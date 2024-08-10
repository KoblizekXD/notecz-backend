package lol.koblizek.notecz.api.auth;

import jakarta.validation.Valid;
import lol.koblizek.notecz.api.auth.data.LoginResponse;
import lol.koblizek.notecz.api.user.UserLoginDto;
import lol.koblizek.notecz.api.user.UserRegistrationDto;
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
    public ResponseEntity<LoginResponse> register(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return new ResponseEntity<>(authService.register(userRegistrationDto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return new ResponseEntity<>(authService.login(userLoginDto), HttpStatus.OK);
    }
}
