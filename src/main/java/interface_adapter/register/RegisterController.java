package interface_adapter.register;

import use_case.register.RegisterInputData;
import use_case.register.RegisterInputBoundary;

public class RegisterController {

    private final RegisterInputBoundary registerInteractor;

    public RegisterController(RegisterInputBoundary registerInteractor){
        this.registerInteractor = registerInteractor;
    }
    public void execute(String email, String username, char[] password, char[] confirmPassword) {
        String passwordString = new String(password);
        String confirmPasswordString = new String(password);
        final RegisterInputData registerInputData = new RegisterInputData(email, username, passwordString,
                confirmPasswordString);
        registerInteractor.execute(registerInputData);
    }

    public void switchToLoginView() {
        registerInteractor.switchToLoginView();
    }
}
