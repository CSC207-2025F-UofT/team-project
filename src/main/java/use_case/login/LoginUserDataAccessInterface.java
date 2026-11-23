package use_case.login;

import entities.User;

public interface LoginUserDataAccessInterface {
    boolean existsByName(String username);
    User get(String username);

}
