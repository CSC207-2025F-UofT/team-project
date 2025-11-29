package entity;

import org.json.JSONObject;

public class Admin_k {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    public Admin_k(int id, String firstName, String lastName, String email, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
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

    public Admin_k(JSONObject adminJson) {
        id = adminJson.getInt("id");
        firstName = adminJson.getString("firstName");
        lastName = adminJson.getString("lastName");
        email = adminJson.getString("email");
        username = adminJson.getString("username");
        password = adminJson.getString("password");
    }

    public JSONObject toJson() {
        JSONObject adminJson = new JSONObject();

        adminJson.put("id", id);
        adminJson.put("firstName", firstName);
        adminJson.put("lastName", lastName);
        adminJson.put("email", email);
        adminJson.put("username", username);
        adminJson.put("password", password);

        return adminJson;
    }
}
