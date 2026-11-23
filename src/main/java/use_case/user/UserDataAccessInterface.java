package use_case.user;

import entity.User;

public interface UserDataAccessInterface {
    boolean existsByName(String username);

    void save(User user);

    User get(String username);
}
