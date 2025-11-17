package usecase.signup;

import usecase.signup.SignupOutputData;

public interface SignupOutputBoundary {
    void signupSuccessView(SignupOutputData signupOutputData);
    void signupFailureView(String errorMessage);
    void switchToLoginView();
}
