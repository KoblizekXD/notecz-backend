package lol.koblizek.notecz.api.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    ;

    @Override
    public String getAuthority() {
        return toString();
    }
}
