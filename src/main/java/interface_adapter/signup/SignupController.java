package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * Controller for the Signup Use Case.
 *
 * Acts as an adapter between the View layer and the Use Case layer.
 * Converts UI data (raw Strings) into use case data (SignupInputData)
 * and triggers the signup process.
 */
public class SignupController {

    private final SignupInputBoundary signupInteractor;

    /**
     * Constructs a SignupController.
     *
     * @param signupInteractor the interactor that handles signup logic
     */
    public SignupController(SignupInputBoundary signupInteractor) {
        this.signupInteractor = signupInteractor;
    }

    /**
     * Executes the Signup Use Case.
     *
     * Takes raw string data from the UI, packages it into the format
     * expected by the use case, and triggers the signup process.
     *
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @param confirmPassword the confirm password entered by the user
     */
    public void execute(String username, String password, String confirmPassword) {
        final SignupInputData signupInputData = new SignupInputData(
                username, password, confirmPassword);

        signupInteractor.execute(signupInputData);
    }
}