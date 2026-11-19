package data_access;

import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * Convenience type for a storage class that supports all three auth use cases.
 * AppBuilder depends on this, so swapping InMemory <-> File storage is one line in Main.
 */
public interface UserDataAccessObject extends SignupUserDataAccessInterface,
        LoginUserDataAccessInterface, LogoutUserDataAccessInterface {
}
