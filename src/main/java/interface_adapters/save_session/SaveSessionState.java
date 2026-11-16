package interface_adapters.save_session;

/**
 * The State information representing the Session.
 */
public class SaveSessionState {
    private String errorMessage = null;

    /**
     * Returns the error message.
     * @return the current error message, or null if no error exists
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Sets the error message in the view state.
     * @param error the error message to be stored
     */
    public void setErrorMessage(String error) {
        this.errorMessage = error;
    }
}
