package entity;

/**
 * The shelter admin. There is a single admin account, identified only by its password.
 */
public class Admin {
    private final String passwordHash;

    public Admin(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
