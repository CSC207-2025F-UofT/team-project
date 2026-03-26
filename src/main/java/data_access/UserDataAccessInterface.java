package data_access;

import entity.User;

public interface UserDataAccessInterface {
    User get(String username);
    boolean existsByName(String username);
    void save(User user);
}
