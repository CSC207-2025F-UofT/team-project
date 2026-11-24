package use_case.account;

import entity.User;

import java.util.Optional;

/** Lets any use case find out who is signed in without depending on how the session is stored. */
public interface CurrentUserReader {
    /** Returns the signed-in user, or empty while signed out. */
    Optional<User> currentUser();
}
