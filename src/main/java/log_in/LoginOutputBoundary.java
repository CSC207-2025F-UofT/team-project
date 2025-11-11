package log_in;

public interface LoginOutputBoundary {
    void PrepareSuccessView(LoginOutputData output);
    void PrepareFailView(String errorMessage);
}
