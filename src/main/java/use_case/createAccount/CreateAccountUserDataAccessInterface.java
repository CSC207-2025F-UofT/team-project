package use_case.createAccount;

import entity.user.User;

public interface CreateAccountUserDataAccessInterface {
    boolean existsByName(String username);

    void save(User user);
}