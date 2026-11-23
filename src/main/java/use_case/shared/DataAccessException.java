package use_case.shared;

/**
 * Thrown by any data access object when it can't read, save or fetch something
 * (database unreadable, save failed, image request failed, missing API key...).
 * Interactors catch it and tell their presenter to show an error.
 */
public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
