package use_case.update_profile;

import entity.User;

public interface UpdateUserProfileUserDataAccessInterface {
    User getCurrentUser();
    void save(User user);
}
