package interface_adapter.enter_name.makeNewPlayer;

/**
 * The state for the NewPlayer View Model.
 */
public class NewPlayerState {
    private String username = "";
    private String usernameError;

    public String getUsername() {
        return username;
    }

    public String getUsernameError() {
        return usernameError;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    @Override
    public String toString() {
        return "SignupState{"
                + "username='" + username + '\''
                + '}';
    }
}
