package interface_adapter.signup;

/**
 * The state information representing the signup form.
 *
 * Stores the current values entered in the signup form fields
 * and any error messages to display to the user.
 */
public class SignupState {
    private String username = "";
    private String password = "";
    private String confirmPassword = "";
    private String usernameError = " ";
    private String passwordError = " ";
    private String successMessage = " ";

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}