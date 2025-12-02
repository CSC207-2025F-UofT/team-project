package entity;

import org.json.JSONObject;

/**
 * Represents an administrator account in the system.
 * This class stores basic admin information such as name, email,
 * username, and password, and supports JSON serialization/deserialization.
 */
public class Admin {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    /**
     * Constructs an Admin_k object using explicit field values.
     *
     * @param id        the admin's unique ID
     * @param firstName the admin's first name
     * @param lastName  the admin's last name
     * @param email     the admin's email address
     * @param username  the admin's login username
     * @param password  the admin's login password
     */
    public Admin(int id, String firstName, String lastName, String email, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs an Admin_k object from a JSON representation.
     *
     * @param adminJson a JSONObject containing admin information,
     *                  expected to include id, firstName, lastName,
     *                  email, username, and password
     */
    public Admin(JSONObject adminJson) {
        id = adminJson.getInt("id");
        firstName = adminJson.getString("firstName");
        lastName = adminJson.getString("lastName");
        email = adminJson.getString("email");
        username = adminJson.getString("username");
        password = adminJson.getString("password");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Converts this admin object into a JSON representation.
     *
     * @return a JSONObject containing all admin fields
     */
    public JSONObject toJson() {
        final JSONObject adminJson = new JSONObject();

        adminJson.put("id", id);
        adminJson.put("firstName", firstName);
        adminJson.put("lastName", lastName);
        adminJson.put("email", email);
        adminJson.put("username", username);
        adminJson.put("password", password);

        return adminJson;
    }
}
