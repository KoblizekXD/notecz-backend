package lol.koblizek.notecz.error;

public record ErrorObject(int code, String message, Object additionalInfo) {
}
