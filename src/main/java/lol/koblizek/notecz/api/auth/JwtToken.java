package lol.koblizek.notecz.api.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lol.koblizek.notecz.api.user.User;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.function.Function;

public record JwtToken(String token, SecretKey secret) {
    public boolean isTokenExpired() {
        return getExpiration().isBefore(Instant.now());
    }

    public String getSubject() {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isValid(User user) {
        return !isTokenExpired() && getSubject().equals(user.getEmail());
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Instant getExpiration() {
        return getClaim(token, Claims::getExpiration).toInstant();
    }

    public Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String toString() {
        return token;
    }
}
