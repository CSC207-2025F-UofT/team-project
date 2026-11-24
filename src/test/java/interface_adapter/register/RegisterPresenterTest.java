package interface_adapter.register;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeState;
import interface_adapter.home.HomeViewModel;
import interface_adapter.register.RegisterState.Field;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import use_case.account.AccountProfile;
import use_case.register.RegisterResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterPresenterTest {
    private final ViewManagerModel viewManager = new ViewManagerModel(RegisterViewModel.VIEW_NAME);
    private final RegisterViewModel register = new RegisterViewModel();
    private final HomeViewModel home = new HomeViewModel();
    private final RegisterPresenter presenter = new RegisterPresenter(viewManager, register, home);

    @Test
    void successShowsTheNewProfileOnHome() {
        presenter.present(new RegisterResponse.Success(new AccountProfile(UUID.randomUUID(), "sam_1", "Sam")));

        assertEquals(new HomeState("Signed in as Sam", "@sam_1"), home.getState());
        assertEquals(RegisterState.empty(), register.getState());
        assertEquals(HomeViewModel.VIEW_NAME, viewManager.getState());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "USERNAME_TAKEN | USERNAME | That username is taken.",
        "PASSWORD_MISMATCH | CONFIRM_PASSWORD | Passwords don't match.",
        "SIGNUP_UNCERTAIN | FORM | We couldn't confirm registration. Try logging in before registering again.",
        "ACCOUNT_CREATED_PROFILE_UNAVAILABLE | FORM | Your account was created, but we couldn't load your profile. "
                + "Try logging in."
    })
    void failureIsShownNextToTheFieldItConcerns(RegisterResponse.Failure failure, Field field, String message) {
        presenter.present(failure);

        assertEquals(new RegisterState(field, message), register.getState());
        assertEquals(RegisterViewModel.VIEW_NAME, viewManager.getState());
    }
}
