package lol.koblizek.notecz.util;

public class UserNotFoundException extends RuntimeException {

    private final String email;

    public UserNotFoundException(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
