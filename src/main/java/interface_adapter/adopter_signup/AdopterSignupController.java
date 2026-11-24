package interface_adapter.adopter_signup;

import use_case.adopter_signup.AdopterSignupInputBoundary;
import use_case.adopter_signup.AdopterSignupInputData;

/**
 * Turns the signup form's input into a call to the signup use case.
 */
public class AdopterSignupController {
    private final AdopterSignupInputBoundary interactor;

    public AdopterSignupController(AdopterSignupInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, String password, String repeatPassword) {
        interactor.execute(new AdopterSignupInputData(username, password, repeatPassword));
    }
}
