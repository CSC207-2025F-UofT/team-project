package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeState;
import interface_adapter.home.HomeViewModel;
import interface_adapter.login.LogInState;
import interface_adapter.login.LogInViewModel;
import org.junit.jupiter.api.Test;
import use_case.account.AccountProfile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogOutPresenterTest {
    @Test
    void clearsTheProfileAndReturnsToLogIn() {
        final ViewManagerModel viewManager = new ViewManagerModel(HomeViewModel.VIEW_NAME);
        final LogInViewModel logIn = new LogInViewModel();
        final HomeViewModel home = new HomeViewModel();
        home.showSignedIn(new AccountProfile(UUID.randomUUID(), "sam_1", "Sam"));

        new LogOutPresenter(viewManager, logIn, home).signedOut();

        assertEquals(HomeState.empty(), home.getState());
        assertEquals(new LogInState(null, "You’re signed out."), logIn.getState());
        assertEquals(LogInViewModel.VIEW_NAME, viewManager.getState());
    }
}
