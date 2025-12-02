package interface_adapter.user_login;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class UserLoginViewModel extends ViewModel<UserLoginState> {
    private String userType;
    
    public UserLoginViewModel() {
        super("login");
        setState(new UserLoginState());
    }
    
    public void setUserType(String type) {
        this.userType = type;
    }

    public String getUserType() {
        return userType;
    }
}
