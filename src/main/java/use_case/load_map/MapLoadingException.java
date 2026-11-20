package use_case.load_map;

/** Reports a map-loading failure without exposing file APIs to the use case. */
public class MapLoadingException extends Exception {
    private static final long serialVersionUID = 1L;

    /** Creates a failure with an explanation suitable for the use case to present. */
    public MapLoadingException(String message) {
        super(message);
    }

    /** Creates a failure while retaining its underlying cause for diagnostics. */
    public MapLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
