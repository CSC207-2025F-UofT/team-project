package use_case.admin_login;

/**
 * Output boundary for the admin login use case.
 */
public interface AdminLoginOutputBoundary {
    void prepareSuccessView();

    void prepareFailView(String errorMessage);
}
