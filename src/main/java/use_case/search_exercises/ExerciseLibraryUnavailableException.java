package use_case.search_exercises;

/**
 * Thrown when the exercise library can't be searched: no internet, the request limit is used up,
 * or the access key is missing or wrong. The use case owns this type, so it can react to the
 * failure without knowing which outside service caused it.
 */
public class ExerciseLibraryUnavailableException extends Exception {

    /**
     * Creates the exception.
     *
     * @param message what went wrong, for logs and debugging
     * @param cause   the underlying error, or {@code null} if there is none
     */
    public ExerciseLibraryUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
