package UseCases.SignUp;

import Boundary.InputBoundary;

public interface SignUpInputBoundary extends InputBoundary{
    /**
     * Executes the signup use case.
     */
    void execute(SignUpInputData inputData);

    /**
     * Executes the switch to login view use case.
     */
    void switchToLoginView();
}
