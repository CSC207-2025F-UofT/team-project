package interface_adapter.login;

/** At most one of {@code error} and {@code notice} is set. */
public record LogInState(String error, String notice) {
    public static LogInState empty() {
        return new LogInState(null, null);
    }
}
