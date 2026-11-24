package interface_adapter.adopter_login;

import use_case.adopter_login.AdopterLoginInputBoundary;
import use_case.adopter_login.AdopterLoginInputData;

/**
 * Turns the login form's input into a call to the adopter login use case.
 */
public class AdopterLoginController {
    private final AdopterLoginInputBoundary interactor;

    public AdopterLoginController(AdopterLoginInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, String password) {
        interactor.execute(new AdopterLoginInputData(username, password));
    }
}
