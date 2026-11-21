package interface_adapter.removeBook;

/**
 * The state for the Remove Book View Model.
 */
public class RemoveBookState {
    private String removedOpenLibraryId;
    private String message;
    private String error;

    public String getRemovedOpenLibraryId() {
        return removedOpenLibraryId;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public void setRemovedOpenLibraryId(String removedOpenLibraryId) {
        this.removedOpenLibraryId = removedOpenLibraryId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "RemoveBookState{"
                + "removedOpenLibraryId='" + removedOpenLibraryId + '\''
                + ", message='" + message + '\''
                + ", error='" + error + '\''
                + '}';
    }
}
