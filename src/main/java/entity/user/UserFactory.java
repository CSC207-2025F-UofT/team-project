package entity.user;

import java.util.ArrayList;

public class UserFactory {
    public User create(String name, String password) {
        // creates a new user with no saved locations
        return new User(name, password, new ArrayList<>());
    }
}
