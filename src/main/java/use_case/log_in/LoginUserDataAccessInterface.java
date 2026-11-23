package use_case.log_in;

import entity.User;

public interface LoginUserDataAccessInterface {
    boolean existsByName(String username);
    User get(String username);
    void setCurrentUsername(String username);
}
