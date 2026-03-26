package entity.user;

import java.util.ArrayList;
import java.util.List;

public class UserFactory {

    public User create(String name, String password) {
        return new User(name, password);
    }

    public User create(String name, String password, List<String> locations) {

        if (locations == null) {
            locations = new ArrayList<>();
        }
        return new User(name, password, locations);
    }
}
