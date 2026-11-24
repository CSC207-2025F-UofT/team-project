package interface_adapter.login;

import use_case.login.LogInInputBoundary;
import use_case.login.LogInRequest;

public final class LogInController {
    private final LogInInputBoundary input;

    public LogInController(LogInInputBoundary input) {
        this.input = input;
    }

    public void logIn(String email, String password) {
        input.execute(new LogInRequest(email, password));
    }
}
