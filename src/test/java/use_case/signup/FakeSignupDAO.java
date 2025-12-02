package use_case.signup;

import entity.User;

public class FakeSignupDAO implements SignupUserDataAccessInterface {

    public boolean exists = false;
    public User savedUser = null;

    @Override
    public boolean existsByName(String username) {
        return exists;
    }

    @Override
    public void save(User user) {
        savedUser = user;
    }
}
