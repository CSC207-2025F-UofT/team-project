package use_case.logout;

/**
 * What the Logout use case needs from storage.
 */
public interface LogoutUserDataAccessInterface {
    String getCurrentUsername();

    void setCurrentUsername(String username);
}
