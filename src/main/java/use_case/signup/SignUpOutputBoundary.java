package use_case.signup;
public interface SignUpOutputBoundary {
    void prepareSuccessView(SignUpOutputData outputData);
    void prepareFailView(String message);
}
