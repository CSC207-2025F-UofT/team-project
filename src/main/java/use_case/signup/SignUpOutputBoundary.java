package use_case.signup;

public interface SignUpOutputBoundary {
    void prepareFailureView(String error);

    void prepareSuccessView(
            SignUpOutputData signUpOutputData);
}
