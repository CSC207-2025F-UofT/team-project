package use_case.account;

import entity.User;

import java.util.Optional;

public interface ProfileGateway {
    /** Returns the profile of the session's user, or empty if it is missing, invalid or cannot be loaded. */
    Optional<User> loadProfile(AuthSession session);
}
