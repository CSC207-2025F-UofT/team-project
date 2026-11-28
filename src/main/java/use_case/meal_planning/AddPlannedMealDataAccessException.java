package use_case.meal_planning;

public class AddPlannedMealDataAccessException extends Exception {

    public AddPlannedMealDataAccessException(String message) {
        super(message);
    }

    public AddPlannedMealDataAccessException(
            String message, Throwable cause) {
        super(message, cause);
    }
}