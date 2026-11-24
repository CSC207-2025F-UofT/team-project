package interface_adapter.Login;

import ViewManager.ViewManagerModel;
import interface_adapter.Loggedin.LoggedinViewModel;
import interface_adapter.Signup.SignupViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.login.LoginOutputData;

import static org.junit.jupiter.api.Assertions.*;

public class LoginPresenterTest {

    private ViewManagerModel viewManagerModel;
    private LoginViewModel loginViewModel;
    private LoggedinViewModel loggedinViewModel;
    private LoginPresenter presenter;

    private int viewChanges;
    private int loginNotifications;
    private int loggedinNotifications;

    @BeforeEach
    void setUp() {
        viewManagerModel = new ViewManagerModel();
        loginViewModel = new LoginViewModel();
        loggedinViewModel = new LoggedinViewModel();
        presenter = new LoginPresenter(viewManagerModel, loginViewModel, loggedinViewModel);

        viewChanges = 0;
        loginNotifications = 0;
        loggedinNotifications = 0;
        viewManagerModel.addListener(() -> viewChanges++);
        loginViewModel.addListener(() -> loginNotifications++);
        loggedinViewModel.addListener(() -> loggedinNotifications++);
    }

    @Test
    void successPassesUserToLoggedinViewModel() {
        presenter.prepareSuccessView(new LoginOutputData("jonny", "Regular User"));

        assertEquals("jonny", loggedinViewModel.getState().getUsername());
        assertEquals("Regular User", loggedinViewModel.getState().getRole());
        assertEquals(1, loggedinNotifications);
    }

    @Test
    void successForResearcherPassesResearcherRole() {
        presenter.prepareSuccessView(new LoginOutputData("dr_smith", "Researcher"));

        assertEquals("Researcher", loggedinViewModel.getState().getRole());
    }

    @Test
    void successStartsWithFreshLoggedinState() {
        loggedinViewModel.getState().setDataLoaded(true);
        loggedinViewModel.getState().setDataError("old error");

        presenter.prepareSuccessView(new LoginOutputData("jonny", "Regular User"));

        assertFalse(loggedinViewModel.getState().isDataLoaded());
        assertEquals("", loggedinViewModel.getState().getDataError());
    }

    @Test
    void successClearsLoginForm() {
        loginViewModel.getState().setUsername("jonny");
        loginViewModel.getState().setLoginError("old error");
        loginViewModel.getState().setWelcomeMessage("old welcome");

        presenter.prepareSuccessView(new LoginOutputData("jonny", "Regular User"));

        assertEquals("", loginViewModel.getState().getUsername());
        assertEquals("", loginViewModel.getState().getLoginError());
        assertEquals("", loginViewModel.getState().getWelcomeMessage());
        assertEquals(1, loginNotifications);
    }

    @Test
    void successSwitchesToLoggedinView() {
        presenter.prepareSuccessView(new LoginOutputData("jonny", "Regular User"));

        assertEquals(LoggedinViewModel.VIEW_NAME, viewManagerModel.getCurrentViewName());
        assertEquals(1, viewChanges);
    }

    @Test
    void failShowsErrorAndClearsWelcomeMessage() {
        loginViewModel.getState().setWelcomeMessage("Hello new Regular User!");

        presenter.prepareFailView("Username cannot be empty.");

        assertEquals("Username cannot be empty.", loginViewModel.getState().getLoginError());
        assertEquals("", loginViewModel.getState().getWelcomeMessage());
        assertEquals(1, loginNotifications);
    }

    @Test
    void failDoesNotChangeViewOrTouchLoggedinViewModel() {
        presenter.prepareFailView("nope");

        assertEquals("", viewManagerModel.getCurrentViewName());
        assertEquals(0, viewChanges);
        assertEquals(0, loggedinNotifications);
    }

    @Test
    void switchToSignupViewChangesView() {
        presenter.switchToSignupView();

        assertEquals(SignupViewModel.VIEW_NAME, viewManagerModel.getCurrentViewName());
        assertEquals(1, viewChanges);
        assertEquals(0, loginNotifications);
    }
}
