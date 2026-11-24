package UseCases.SignUp;

import Boundary.OutputBoundary;

public interface SignUpOutputBoundary extends OutputBoundary{

    /**
     * Prepares the success view for the Signup Use Case.
     */
    void prepareSuccessView(SignUpOutputData outputData);

    /**
     * Prepares the failure view for the Signup Use Case.
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Login View.
     */
    void switchToLoginView();
}
