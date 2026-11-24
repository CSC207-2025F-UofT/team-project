package Entities;

public class User {
    private String username;
    private String password;
    private UserProfile profile;
    private Organization[] organizations;
    private Department[] departments;

    private User(String username, String password, UserProfile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
        this.organizations = new Organization[0];
        this.departments = new Department[0];
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getUserProfile() {
        return profile;
    }

    public Organization[] getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Organization[] organizations) {
        this.organizations = organizations;
    }

    public Department[] getDepartments() {
        return departments;
    }

    public void setDepartments(Department[] departments) {
        this.departments = departments;
    }

    public static User UserFactory(String username, String password, UserProfile profile) throws IllegalArgumentException {
        if (username == null) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password is required.");
        }

        return new User(username, password, profile);
    }

}