package use_case.user_login;

import entity.User;

public class FakeUserLoginDAO implements UserLoginUserDataAccessInterface {

    public boolean exists = false;
    public User returnUser = null;
    public String currentUserSet = null;

    @Override
    public boolean existsByName(String username) {
        return exists;
    }

    @Override
    public void save(User user) {
        // not needed for login tests
    }

    @Override
    public User get(String username) {
        return returnUser;
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUserSet = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUserSet;
    }
}
