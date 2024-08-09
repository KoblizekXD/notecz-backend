package lol.koblizek.notecz.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorObject> constraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(new ErrorObject(400, "Validation failed", e.getConstraintViolations().stream().map(ConstraintViolation::getInvalidValue)));
    }
}
