package use_case.register;

public record RegisterRequest(String email, String username, String displayName, String password,
                              String passwordConfirmation) {
    @Override
    public String toString() {
        return "RegisterRequest[redacted]";
    }
}
