package use_case.change_username;

import entity.User;

public interface ChangeUsernameUserDataAccessInterface {
    boolean existsByName(String identifier);
    void changeUsername(String oldUsername, String newUsername);
}