package use_case.login;

public record LogInRequest(String email, String password) {
    @Override
    public String toString() {
        return "LogInRequest[redacted]";
    }
}
