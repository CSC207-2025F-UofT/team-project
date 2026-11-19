package use_case.logout;

/**
 * Output boundary for the Logout use case (implemented by LogoutPresenter).
 */
public interface LogoutOutputBoundary {
    void prepareSuccessView(LogoutOutputData outputData);

    void prepareFailView(String errorMessage);
}
