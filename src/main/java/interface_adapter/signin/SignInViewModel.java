package interface_adapter.signin;

import interface_adapter.ViewModel;

public class SignInViewModel extends ViewModel<SignInState> {

    public SignInViewModel() {
        super("Sign in view");
        setState(new SignInState());
    }
}
