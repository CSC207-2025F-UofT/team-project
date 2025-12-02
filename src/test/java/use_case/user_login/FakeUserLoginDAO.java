package use_case.user_login;

import entity.User;


public class FakeUserLoginDAO implements UserLoginUserDataAccessInterface {

    private final java.util.Map<String, User> storage = new java.util.HashMap<>();
    public String currentUsername;

    void saveUser(User u) {
        storage.put(u.getUsername(), u);
    }

    @Override
    public boolean existsByName(String username) {
        return storage.containsKey(username);
    }

    @Override
    public void save(User user) {

    }

    @Override
    public User get(String username) {
        return storage.get(username);
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public String getCurrentUsername() {
        return "";
    }
}