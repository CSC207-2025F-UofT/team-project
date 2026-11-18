package Entity;

public class User {

    public enum Role {
        ADOPTER,
        ADMIN
    }

    private final String name;
    private final String id;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private final String preference;

    /**
     * Creates a new user.
     *
     * @param name         the user's display name; must not be null or empty
     * @param id           the unique identifier of the user; must not be null or empty
     * @param email        the user's email address; must not be null or empty
     * @param passwordHash the hashed password (never the raw password); must not be null or empty
     * @param role         the user's role; must not be null
     * @param preference   the adopter's preference, or {@code null} if none;
     *                     must be {@code null} when {@code role} is {@link Role#ADMIN}
     * @throws IllegalArgumentException if any required field is null or empty,
     *                                  or if an admin is given a non-null preference
     */
    public User(String name, String id, String email, String passwordHash, Role role, String preference) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(id)) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        if ("".equals(email)) {
            throw new IllegalArgumentException("email cannot be empty");
        }
        if ("".equals(passwordHash)) {
            throw new IllegalArgumentException("password cannot be empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("role cannot be empty");
        }
        if (role == Role.ADMIN && preference != null) {
            throw new IllegalArgumentException("ADMIN should not have a preference");
        }
        this.name = name;
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        if (this.role == Role.ADMIN) {
            this.preference = null;
        }
        else{
            this.preference = preference;
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public String getPreference() {
        return preference;
    }
}
