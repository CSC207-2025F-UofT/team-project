package interface_adapter.user_login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class UserLoginViewModel extends ViewModel<UserLoginState> {

    public UserLoginViewModel() {
        super("log in");
        setState(new UserLoginState());
    }

}