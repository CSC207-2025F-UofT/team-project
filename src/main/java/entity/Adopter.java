package entity;

/**
 * An adopter account.
 * Only the hashed password is stored, never the raw password.
 */
public class Adopter {
    private final String username;
    private final String passwordHash;

    public Adopter(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
