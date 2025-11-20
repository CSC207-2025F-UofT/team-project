package use_case.login;

import entity.User;

public interface LoginDataAccessInterface {

    boolean existsByUsername(String username);

    User getByUsername(String username);

}
