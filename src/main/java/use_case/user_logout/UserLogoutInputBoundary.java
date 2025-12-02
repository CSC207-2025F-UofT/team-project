package use_case.user_logout;

/**
 * Input Boundary for the logout use case.
 */
public interface UserLogoutInputBoundary {

    /**
     * Executes the Logout use case. After this executes,
     * there will be no logged-in user.
     */
    void execute();
}
