package use_case.signup;

import use_case.login.LoginInputData;

/**
 * Input Boundary for actions which are related to signing up.
 */
public interface SignupInputBoundary {
    void execute(SignupInputData signupInputData);
}
