package use_case.update_profile;

import dataAccess.FileUserDataAccessObject;
import entity.User;

public class FakeUserDAO {

    private User current;

    public FakeUserDAO(User initial) {
        this.current = initial;
    }

    public User getCurrentUser() {
        return current;
    }

    public void save(User user) {
        this.current = user;
    }

    public void updateUser(User user) {
        this.current = user;
    }
}
