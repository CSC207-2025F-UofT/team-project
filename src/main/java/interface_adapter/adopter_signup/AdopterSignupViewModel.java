package interface_adapter.adopter_signup;

import interface_adapter.ViewModel;

/**
 * View model for the adopter signup screen.
 */
public class AdopterSignupViewModel extends ViewModel<AdopterSignupState> {
    public static final String VIEW_NAME = "adopter signup";

    public AdopterSignupViewModel() {
        super(VIEW_NAME);
        setState(new AdopterSignupState());
    }
}
