package use_case.signup;

public class SignupOutputBoundary {
    private final String username;
    private final boolean useCaseFailed;

    public SignupOutputBoundary(String username, boolean useCaseFailed) {
        this.username = username;
        this.useCaseFailed = useCaseFailed;
    }
    public String getUsername() {
        return username;
    }
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
