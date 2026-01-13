package interface_adapter.signin;

import use_case.signIn.SignInInputBoundary;
import use_case.signIn.SigninInputData;


public class SignInController {
    private SignInViewModel signInViewModel;
    private final SignInInputBoundary signinInputBoundary;

    public SignInController(SignInInputBoundary signinInputBoundary) {
        this.signinInputBoundary = signinInputBoundary;
    }

    public void execute(String username, String password) {
        final SigninInputData signinInputData = new SigninInputData(username, password);
        signinInputBoundary.execute(signinInputData);

    }

    public void switchToHomePage() {
        signinInputBoundary.switchToHomePage();
    }
}