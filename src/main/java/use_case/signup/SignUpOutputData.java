package use_case.signup;

public class SignUpOutputData {

    private final boolean success;
    private final String message;

    public SignUpOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
