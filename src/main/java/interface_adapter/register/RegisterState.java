package interface_adapter.register;

/** {@code errorField} says where to show {@code error}; both are null when there is nothing to report. */
public record RegisterState(Field errorField, String error) {
    public enum Field {
        FORM, EMAIL, USERNAME, DISPLAY_NAME, PASSWORD, CONFIRM_PASSWORD
    }

    public static RegisterState empty() {
        return new RegisterState(null, null);
    }
}
