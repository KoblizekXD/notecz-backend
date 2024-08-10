package lol.koblizek.notecz.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserLoginDto(
        @NotNull @Email(message = "Invalid email") String email,
        @NotNull String password
) {
}
