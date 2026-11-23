package use_case.continue_game;

/**
 * Thrown when a saved checkpoint is damaged or in an unsupported format.
 */
public class InvalidCheckpointException extends Exception {
    public InvalidCheckpointException(String message) {
        super(message);
    }

    public InvalidCheckpointException(String message, Throwable cause) {
        super(message, cause);
    }
}
