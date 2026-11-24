package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeState;
import interface_adapter.home.HomeViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import use_case.account.AccountProfile;
import use_case.login.LogInResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogInPresenterTest {
    private final ViewManagerModel viewManager = new ViewManagerModel(LogInViewModel.VIEW_NAME);
    private final LogInViewModel logIn = new LogInViewModel();
    private final HomeViewModel home = new HomeViewModel();
    private final LogInPresenter presenter = new LogInPresenter(viewManager, logIn, home);

    @Test
    void successShowsTheProfileOnHome() {
        logIn.setState(new LogInState("Earlier error", null));

        presenter.present(new LogInResponse.Success(new AccountProfile(UUID.randomUUID(), "sam_1", "Sam")));

        assertEquals(new HomeState("Signed in as Sam", "@sam_1"), home.getState());
        assertEquals(LogInState.empty(), logIn.getState());
        assertEquals(HomeViewModel.VIEW_NAME, viewManager.getState());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "BAD_CREDENTIALS | Email or password is incorrect.",
        "NO_USABLE_SESSION | Confirm your email if prompted, then try logging in again.",
        "RETRYABLE_FAILURE | We couldn't log you in right now. Please try again."
    })
    void failureExplainsWhatToTryNextAndStaysOnLogIn(LogInResponse.Failure failure, String message) {
        presenter.present(failure);

        assertEquals(new LogInState(message, null), logIn.getState());
        assertEquals(LogInViewModel.VIEW_NAME, viewManager.getState());
    }
}
