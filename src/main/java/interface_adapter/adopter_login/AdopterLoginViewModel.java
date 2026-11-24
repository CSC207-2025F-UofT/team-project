package interface_adapter.adopter_login;

import interface_adapter.ViewModel;

/**
 * View model for the adopter login screen.
 */
public class AdopterLoginViewModel extends ViewModel<AdopterLoginState> {
    public static final String VIEW_NAME = "adopter login";

    public AdopterLoginViewModel() {
        super(VIEW_NAME);
        setState(new AdopterLoginState());
    }
}
