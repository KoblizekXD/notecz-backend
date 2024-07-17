package lol.koblizek.notecz.util;

import jakarta.validation.ConstraintViolationException;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConstraintOptional<T> {

    private final T t;
    private final Map<String, String> violations;

    ConstraintOptional(T t) {
        this.t = t;
        this.violations = Map.of();
    }

    ConstraintOptional(ConstraintViolationException e) {
        this.t = null;
        violations = new HashMap<>();
        e.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            violations.put(fieldName, errorMessage);
        });
    }

    public ConstraintOptional() {
        this.t = null;
        this.violations = Map.of();
    }

    public static <E> ConstraintOptional<E> of(@NonNull E e) {
        Objects.requireNonNull(e);
        return new ConstraintOptional<>(e);
    }

    public static <E> ConstraintOptional<E> violated(ConstraintViolationException e) {
        return new ConstraintOptional<>(e);
    }

    public boolean isPresent() {
        return t != null;
    }

    public T get() {
        return t;
    }

    public Map<String, String> getViolations() {
        return violations;
    }

    public boolean hasViolations() {
        return !violations.isEmpty();
    }
}
