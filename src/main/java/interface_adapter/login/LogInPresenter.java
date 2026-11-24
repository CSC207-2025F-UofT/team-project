package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import use_case.login.LogInOutputBoundary;
import use_case.login.LogInResponse;

public final class LogInPresenter implements LogInOutputBoundary {
    private final ViewManagerModel viewManager;
    private final LogInViewModel logIn;
    private final HomeViewModel home;

    public LogInPresenter(ViewManagerModel viewManager, LogInViewModel logIn, HomeViewModel home) {
        this.viewManager = viewManager;
        this.logIn = logIn;
        this.home = home;
    }

    @Override
    public void present(LogInResponse response) {
        switch (response) {
            case LogInResponse.Success(var profile) -> {
                home.showSignedIn(profile);
                logIn.setState(LogInState.empty());
                viewManager.setState(home.getViewName());
            }
            case LogInResponse.Failure failure -> logIn.setState(new LogInState(message(failure), null));
        }
    }

    private static String message(LogInResponse.Failure failure) {
        return switch (failure) {
            case BAD_CREDENTIALS -> "Email or password is incorrect.";
            case NO_USABLE_SESSION -> "Confirm your email if prompted, then try logging in again.";
            case RETRYABLE_FAILURE -> "We couldn't log you in right now. Please try again.";
        };
    }
}
