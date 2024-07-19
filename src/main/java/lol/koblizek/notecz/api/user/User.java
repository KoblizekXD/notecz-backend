package lol.koblizek.notecz.api.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lol.koblizek.notecz.api.auth.Permission;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Column(name = "name")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(targetClass = Permission.class)
    @JoinTable(name = "permissions", joinColumns = @JoinColumn(name = "userId"))
    @Column(name = "permissions", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

    public User() {
        this.permissions = new HashSet<>();
    }

    public User(String username, String name, String email, String password, Permission... permissions) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.permissions = Set.of(permissions);
    }

    /**
     * Minimal constructor for creating a valid user
     * @param username Username
     * @param email Email
     * @param password Password(plain text)
     */
    public User(String username, String email, String password, Permission... permissions) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.permissions = Set.of(permissions);
    }

    public User(Long id, String username, String name, String email, String password, Permission... permissions) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.permissions = Set.of(permissions);
    }

    @Override
    public Set<Permission> getAuthorities() {
        return permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public static final class UserBuilder {
        private String username;
        private String name;
        private String email;
        private String password;
        private Set<Permission> permissions;

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder permissions(Permission... permissions) {
            this.permissions = Set.of(permissions);
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPermissions(permissions);
            return user;
        }
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }
}