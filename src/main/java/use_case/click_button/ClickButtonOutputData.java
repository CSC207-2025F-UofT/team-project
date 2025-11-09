package use_case.click_button;

/**
 * Output data for the Click Button use case.
 */
public class ClickButtonOutputData {

    private final String message;

    public ClickButtonOutputData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
