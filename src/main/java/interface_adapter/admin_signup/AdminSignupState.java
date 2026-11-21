package interface_adapter.admin_signup;

/**
 * The state for the Admin Signup View Model.
 */
public class AdminSignupState {
    private String username = "";
    private String signupError;
    private String id = "";
    private String idError;
    private String email = "";
    private String emailError;
    private String password = "";
    private String passwordError;
    private String repeatPassword = "";
    private String repeatPasswordError;
    private String inviteCode = "";
    private String inviteCodeError;

    public String getUsername() {
        return username;
    }

    public String getSignupError() {
        return signupError;
    }

    public String getId() {return id;}

    public String getIdError() {return idError;}

    public String getEmail() {return email;}

    public String getEmailError() {return emailError;}

    public String getPassword() {
        return password;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public String getRepeatPasswordError() {
        return repeatPasswordError;
    }

    public String getInviteCode() {return inviteCode;}

    public String getInviteCodeError() {return inviteCodeError;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSignupError(String signupError) {
        this.signupError = signupError;
    }

    public void setId(String id) {this.id = id;}

    public void setIdError(String idError) {this.idError = idError;}

    public void setEmail(String email) {this.email = email;}

    public void setEmailError(String emailError) {this.emailError = emailError;}

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setRepeatPasswordError(String repeatPasswordError) {
        this.repeatPasswordError = repeatPasswordError;
    }

    public void setInviteCode(String inviteCode) {this.inviteCode = inviteCode;}

    public void setInviteCodeError(String inviteCodeError) {this.inviteCodeError = inviteCodeError;}

    @Override
    public String toString() {
        return "SignupState{"
                + "username='" + username + '\''
                + "id='" + id + '\''
                + "email ='" + email +'\''
                + ", password='" + password + '\''
                + ", repeatPassword='" + repeatPassword + '\''
                + ", inviteCode='" + inviteCode + '\''
                + '}';
    }
}
