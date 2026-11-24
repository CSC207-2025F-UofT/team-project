package interface_adapter.register;

import use_case.register.RegisterInputBoundary;
import use_case.register.RegisterRequest;

public final class RegisterController {
    private final RegisterInputBoundary input;

    public RegisterController(RegisterInputBoundary input) {
        this.input = input;
    }

    public void register(String email, String username, String displayName, String password, String confirmation) {
        input.execute(new RegisterRequest(email, username, displayName, password, confirmation));
    }
}
