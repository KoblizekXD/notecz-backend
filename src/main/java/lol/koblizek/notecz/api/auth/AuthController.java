package lol.koblizek.notecz.api.auth;

import lol.koblizek.notecz.api.auth.data.LoginResponse;
import lol.koblizek.notecz.api.user.UserDto;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/register")
    public LoginResponse register(@RequestBody @Validated UserDto userDto) {
        return authService.register(userDto);
    }
}
