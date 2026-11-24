package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeState;
import interface_adapter.home.HomeViewModel;
import interface_adapter.login.LogInState;
import interface_adapter.login.LogInViewModel;
import use_case.logout.LogOutOutputBoundary;

public final class LogOutPresenter implements LogOutOutputBoundary {
    private final ViewManagerModel viewManager;
    private final LogInViewModel logIn;
    private final HomeViewModel home;

    public LogOutPresenter(ViewManagerModel viewManager, LogInViewModel logIn, HomeViewModel home) {
        this.viewManager = viewManager;
        this.logIn = logIn;
        this.home = home;
    }

    @Override
    public void signedOut() {
        home.setState(HomeState.empty());
        logIn.setState(new LogInState(null, "You’re signed out."));
        viewManager.setState(logIn.getViewName());
    }
}
