package interface_adapter.register;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.register.RegisterState.Field;
import use_case.register.RegisterOutputBoundary;
import use_case.register.RegisterResponse;

public final class RegisterPresenter implements RegisterOutputBoundary {
    private final ViewManagerModel viewManager;
    private final RegisterViewModel register;
    private final HomeViewModel home;

    public RegisterPresenter(ViewManagerModel viewManager, RegisterViewModel register, HomeViewModel home) {
        this.viewManager = viewManager;
        this.register = register;
        this.home = home;
    }

    @Override
    public void present(RegisterResponse response) {
        switch (response) {
            case RegisterResponse.Success(var profile) -> {
                home.showSignedIn(profile);
                register.setState(RegisterState.empty());
                viewManager.setState(home.getViewName());
            }
            case RegisterResponse.Failure failure -> register.setState(error(failure));
        }
    }

    private static RegisterState error(RegisterResponse.Failure failure) {
        return switch (failure) {
            case INVALID_EMAIL -> new RegisterState(Field.EMAIL, "Enter a valid email address.");
            case INVALID_USERNAME ->
                    new RegisterState(Field.USERNAME, "Use 3–24 letters (A–Z), digits or underscores.");
            case INVALID_DISPLAY_NAME -> new RegisterState(Field.DISPLAY_NAME, "Enter a display name.");
            case PASSWORD_TOO_SHORT -> new RegisterState(Field.PASSWORD, "Use at least 8 characters.");
            case PASSWORD_REJECTED -> new RegisterState(Field.PASSWORD, "Choose a different password.");
            case PASSWORD_MISMATCH -> new RegisterState(Field.CONFIRM_PASSWORD, "Passwords don't match.");
            case USERNAME_TAKEN -> new RegisterState(Field.USERNAME, "That username is taken.");
            case ACCOUNT_UNAVAILABLE -> new RegisterState(Field.FORM,
                    "We couldn't register with those details. Check them or try logging in.");
            case SIGNUP_UNCERTAIN -> new RegisterState(Field.FORM,
                    "We couldn't confirm registration. Try logging in before registering again.");
            case NO_USABLE_SESSION -> new RegisterState(Field.FORM, "Confirm your email if prompted, then log in.");
            case ACCOUNT_CREATED_PROFILE_UNAVAILABLE -> new RegisterState(Field.FORM,
                    "Your account was created, but we couldn't load your profile. Try logging in.");
            case RETRYABLE_FAILURE -> new RegisterState(Field.FORM,
                    "We couldn't complete registration. Please try again later.");
        };
    }
}
