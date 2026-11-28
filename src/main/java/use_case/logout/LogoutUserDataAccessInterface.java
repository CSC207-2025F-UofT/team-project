package use_case.logout;

/**
 * DAO interface for the Log Out Use Case.
 */
public interface LogoutUserDataAccessInterface {

    /**
     * Returns the id of the user who is logged in.
     * @return the id of the logged-in user, or null if no user is logged in
     */
    String getCurrentUserId();

    /**
     * Records which user is logged in.
     * @param id the id of the logged-in user, or null when no user is logged in
     */
    void setCurrentUserId(String id);
}
