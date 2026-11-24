package use_case.change_password;

/**
 * The output boundary for the Change Password Use Case.
 */
public interface ChangePasswordOutputBoundary {

    /** The password was changed. */
    void prepareSuccessView(ChangePasswordOutputData outputData);

    /** The password was not changed, and errorMessage says why. */
    void prepareFailView(String errorMessage);
}
