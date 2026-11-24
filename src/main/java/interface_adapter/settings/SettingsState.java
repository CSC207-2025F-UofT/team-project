package interface_adapter.settings;

/**
 * The State information for the Settings View.
 * Only one of passwordError and passwordMessage is set at a time.
 */
public class SettingsState {

    private final String passwordError;
    private final String passwordMessage;

    public SettingsState(String passwordError, String passwordMessage) {
        this.passwordError = passwordError;
        this.passwordMessage = passwordMessage;
    }

    /** A blank state: no error, no message. */
    public static SettingsState empty() {
        return new SettingsState(null, null);
    }

    public String getPasswordError() {
        return passwordError;
    }

    public String getPasswordMessage() {
        return passwordMessage;
    }
}
