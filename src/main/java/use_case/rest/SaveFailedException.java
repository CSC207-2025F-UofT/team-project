package use_case.rest;

/**
 * Thrown when the checkpoint could not be saved.
 */
public class SaveFailedException extends Exception {
    public SaveFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
