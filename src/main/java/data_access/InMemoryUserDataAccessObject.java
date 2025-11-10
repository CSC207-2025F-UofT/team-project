package data_access;

import use_case.edit_profile.EditProfileDataAccessInterface;
import entities.User;

/**
 * In-memory implementation of user data access.
 * This is a placeholder until the actual data persistence layer is implemented.
 */
public class InMemoryUserDataAccessObject implements EditProfileDataAccessInterface {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void updateUserProfile(String username, String fullName, String bio, String profilePicture) {
        User user = users.get(username);
        if (user != null) {
            user.editProfile(fullName, bio, profilePicture);
        }
    }
}
