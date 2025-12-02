package use_case.update_profile;

import entity.User;
import service.Backend;

public class FakeBackend extends Backend {
    private User current;

    @Override
    public void setCurrentUser(User user) {
        this.current = user;
    }

    @Override
    public User getCurrentUser() {
        return current;
    }
}
