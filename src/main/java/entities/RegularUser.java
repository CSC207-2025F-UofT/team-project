package entities;

/**
 * An entity representing a regular user, extending abstract User.
 */
public class RegularUser extends User {
    private Boolean doneTest = false;

    public RegularUser(String username, String role) {
        super(username, role);
    }

    /**
     * Indicates a User has completed a cognitive test today
     */
    private void doneTest() { this.doneTest = true; }

    @Override
    public String getRole() { return "Regular User"; }

}
