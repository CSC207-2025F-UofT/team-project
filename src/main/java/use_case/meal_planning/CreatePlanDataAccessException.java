package use_case.meal_planning;

/**
 * Indicates that plan storage could not be read or written.
 */
public class CreatePlanDataAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public CreatePlanDataAccessException(String message) {
        super(message);
    }

    public CreatePlanDataAccessException(String message,
                                         Throwable cause) {
        super(message, cause);
    }
}