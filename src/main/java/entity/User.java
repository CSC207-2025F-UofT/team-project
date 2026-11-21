package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A user account and its associated projects.
 */
public class User {
    private final String userId;
    private String displayName;
    private String email;
    private String passwordHash;
    private final List<Project> myProjects;

    /**
     * Creates a user with no projects.
     *
     * @param userId the unique user identifier
     * @param displayName the user's display name
     * @param email the user's email address
     * @param passwordHash the already hashed password
     */
    public User(String userId, String displayName,
                String email, String passwordHash) {
        this.userId = requireNonBlank(userId, "User ID");
        this.displayName = requireNonBlank(displayName, "Display name");
        this.email = requireNonBlank(email, "Email");
        this.passwordHash = requireNonBlank(passwordHash, "Password hash");
        this.myProjects = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = requireNonBlank(displayName, "Display name");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = requireNonBlank(email, "Email");
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = requireNonBlank(passwordHash, "Password hash");
    }

    /**
     * Returns an unmodifiable copy of this user's project list.
     *
     * @return the user's projects
     */
    public List<Project> getMyProjects() {
        return Collections.unmodifiableList(new ArrayList<>(myProjects));
    }

    /**
     * Adds a project unless it is already in the list.
     *
     * @param project the project to add
     * @throws IllegalArgumentException if project is null
     */
    public void addProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }

        if (!myProjects.contains(project)) {
            myProjects.add(project);
        }
    }

    /**
     * Removes a project from this user's list.
     *
     * @param project the project to remove
     * @return true if the project was removed
     * @throws IllegalArgumentException if project is null
     */
    public boolean removeProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }

        return myProjects.remove(project);
    }

    /**
     * Checks that a string is neither null nor blank.
     *
     * @param value the value to check
     * @param fieldName the field name used in the error message
     * @return the valid value
     * @throws IllegalArgumentException if value is null or blank
     */
    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank.");
        }

        return value;
    }
}