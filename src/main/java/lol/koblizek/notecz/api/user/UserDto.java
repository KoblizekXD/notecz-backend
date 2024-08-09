package lol.koblizek.notecz.api.user;

public record UserDto(String username, String email, String password) {
    public User toUser() {
        return new User(username, email, password);
    }
}
