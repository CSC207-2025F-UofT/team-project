package entity.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private final String name;
    private final String password;
    private final List<String> locations;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password) {
        this(name, password, new ArrayList<>());
    }

    public User(String name, String password, List<String> locations) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        this.name = name;
        this.password = password;
        // copy list to protect internal state
        this.locations = new ArrayList<>(locations);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getLocations() {
        // read-only view from outside
        return Collections.unmodifiableList(locations);
    }

    public void addLocation(String location) {
        this.locations.add(location);
    }

    public void removeLocation(String location) {
        this.locations.remove(location);
    }

}
