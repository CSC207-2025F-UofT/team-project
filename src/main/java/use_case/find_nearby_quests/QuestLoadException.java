package use_case.find_nearby_quests;

/** Thrown when the quests cannot be loaded (extension 2a). */
public class QuestLoadException extends Exception {
    /**
     * Creates the exception.
     *
     * @param message what went wrong
     * @param cause the underlying error, or null
     */
    public QuestLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
