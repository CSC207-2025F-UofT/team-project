package entity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    public enum idType {PHOTO_CARD, DRIVERS_LICENSE, PASSPORT}

    private final int id;
    private final String name;
    private final String address;
    private final idType idType;
    private final int phoneNumber;
    private final String email;
    private final String username;
    private final String password;
    private final String userType;

    private final List<Integer> applications = new ArrayList<>();
    private final List<Integer> wishlist = new ArrayList<>();

    public User(int id, String name, String address, idType idType, int phoneNumber, String email, String username, String password, String userType) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.idType = idType;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType =  userType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public idType getIdType() {
        return idType;
    }

    public int getPhoneNumber() {
        return phoneNumber;
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

    public String getUserType() {
    	return userType;
    }

    public User(JSONObject userJson) {
        this.id = userJson.getInt("id");
        this.name = userJson.getString("name");
        this.address = userJson.getString("address");
        this.idType = userJson.getEnum(idType.class, "idType");
        this.phoneNumber = userJson.getInt("phoneNumber");
        this.email = userJson.getString("email");
        this.username = userJson.getString("username");
        this.password = userJson.getString("password");
        this.userType = userJson.getString("userType");
    }

    public JSONObject toJson() {
        JSONObject userJson = new JSONObject();

        userJson.put("id", id);
        userJson.put("name", name);
        userJson.put("address", address);
        userJson.put("idType", idType.toString());
        userJson.put("phoneNumber", phoneNumber);
        userJson.put("email", email);
        userJson.put("username", username);
        userJson.put("password", password);
        userJson.put("userType", userType);

        return userJson;
    }
}
