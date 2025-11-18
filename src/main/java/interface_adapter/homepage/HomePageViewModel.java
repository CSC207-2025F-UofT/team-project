package interface_adapter.homepage;

import interface_adapter.ViewModel;

public class HomePageViewModel extends ViewModel<HomePageState> {
    public static final String TO_CREATE_ACCOUNT_LABEL = "Go to Create Account";
    public static final String TO_LOGIN_LABEL = "Log In";

    public HomePageViewModel() {
        super("Home Page");
        setState(new HomePageState());
    }
}
