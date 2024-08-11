package lol.koblizek.notecz.api.auth.data;

import lol.koblizek.notecz.api.auth.Permission;

import java.time.Instant;
import java.util.Set;

public record LoginResponse(String token, Instant validUntil, Set<Permission> permissions) {
}
