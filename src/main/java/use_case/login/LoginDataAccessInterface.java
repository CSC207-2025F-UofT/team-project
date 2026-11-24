package use_case.login;

import entity.UserProfile;

/** Implemented by a Frameworks and Drivers data access class. */
public interface LoginDataAccessInterface {
    UserProfile findUserByUsername(String username);
    boolean passwordMatches(UserProfile userProfile, String rawPassword);
}
