package interface_adapter.createAccount;

import interface_adapter.ViewModel;

public class CreateAccountViewModel extends ViewModel<CreateAccountState> {

    public static final String TITLE_LABEL = "Sign Up View";
    public static final String USERNAME_LABEL = "Choose username";
    public static final String PASSWORD_LABEL = "Choose password";
    public static final String REPEAT_PASSWORD_LABEL = "Enter password again";

    public static final String SIGNUP_BUTTON_LABEL = "Sign up";
    public static final String GO_BACK_BUTTON_LABEL = "Go Back";

    public CreateAccountViewModel() {
        super("create account");
        setState(new CreateAccountState());
    }

}
