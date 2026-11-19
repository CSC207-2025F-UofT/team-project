package use_case.logout;

/**
 * Input boundary for the Logout use case.
 * No input data needed: it logs out whoever is currently logged in.
 */
public interface LogoutInputBoundary {
    void execute();
}
