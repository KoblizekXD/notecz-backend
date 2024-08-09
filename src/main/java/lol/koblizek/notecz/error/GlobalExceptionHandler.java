package lol.koblizek.notecz.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lol.koblizek.notecz.util.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorObject> constraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(new ErrorObject(400, "Validation failed", Pair.of("violations", e.getConstraintViolations().stream().map(ConstraintViolation::getInvalidValue))));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> violations = e.getBindingResult().getFieldErrors().stream().map(FieldError::getField).toList();
        LOGGER.error("Validation failed for object {}, following fields were violated: {}", e.getObjectName(), violations);

        return ResponseEntity.badRequest().body(new ErrorObject(400, "Validation failed", Pair.of("violations", violations)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> exception(Exception e) {
        return ResponseEntity.status(500).body(new ErrorObject(500, "Internal server error", Pair.of("message", e.getMessage())));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorObject> userAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(new ErrorObject(400, "User already exists(email duplication)", Pair.of("username", e.getMessage())));
    }
}
