package use_case.edit_card;

public class EditCardDataAccessException extends RuntimeException {
    public EditCardDataAccessException(String message) {
        super(message);
    }

    public EditCardDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
