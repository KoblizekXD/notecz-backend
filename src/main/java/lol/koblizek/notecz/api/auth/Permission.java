package lol.koblizek.notecz.api.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    /**
     * Permission to create posts
     */
    CREATE_POSTS,
    /**
     * Permission to change or delete posts created by the user
     */
    MODIFY_POSTS,
    /**
     * Permission to change user's profile
     */
    MODIFY_PROFILE;

    @Override
    public String getAuthority() {
        return name();
    }

    public static Permission[] getStandardUserPermissions() {
        return new Permission[] {
                CREATE_POSTS,
                MODIFY_POSTS,
                MODIFY_PROFILE
        };
    }
}
