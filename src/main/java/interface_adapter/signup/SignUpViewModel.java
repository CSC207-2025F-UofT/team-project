package interface_adapter.signup;

public class SignUpViewModel {
    private String message = "";
    private boolean successful;

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }
}
