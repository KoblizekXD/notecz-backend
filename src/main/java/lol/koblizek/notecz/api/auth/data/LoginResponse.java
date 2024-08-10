package lol.koblizek.notecz.api.auth.data;

import lol.koblizek.notecz.api.auth.Permission;

import java.time.Instant;

public record LoginResponse(String token, Instant validUntil, Permission... permissions) {
}
