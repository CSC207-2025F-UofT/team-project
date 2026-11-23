package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * Stores the state of the login screen.
 */
public class LoginViewModel extends ViewModel<LoginViewModel.LoginState>{

    public static final String VIEW_NAME = "login";

    public LoginViewModel(){
        super(VIEW_NAME);
        setState(new LoginState());
    }

    public static class LoginState{

        private String username = "";
        private String errorMessage = "";

        public String getUsername(){
            return username;
        }

        public void setUsername(String username){
            this.username = username;
        }

        public String getErrorMessage(){
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage){
            this.errorMessage = errorMessage;
        }
    }
}
