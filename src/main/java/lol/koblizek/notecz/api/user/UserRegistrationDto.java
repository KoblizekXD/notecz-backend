package lol.koblizek.notecz.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
        @NotNull @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters") String username,
        @NotNull @Email(message = "Invalid email") String email,
        @NotNull String password
) {
    public User toUser() {
        return new User(username, email, password);
    }
}
