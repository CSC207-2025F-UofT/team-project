package use_case;

/** The save store could not complete the requested operation. */
public class GamePersistenceException extends Exception {
    public GamePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
