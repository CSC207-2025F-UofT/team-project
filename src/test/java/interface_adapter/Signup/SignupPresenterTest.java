package interface_adapter.Signup;

import ViewManager.ViewManagerModel;
import interface_adapter.Login.LoginViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.signup.SignupOutputData;

import static org.junit.jupiter.api.Assertions.*;

public class SignupPresenterTest {

    private ViewManagerModel viewManagerModel;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private SignupPresenter presenter;

    private int viewChanges;
    private int signupNotifications;
    private int loginNotifications;

    @BeforeEach
    void setUp() {
        viewManagerModel = new ViewManagerModel();
        signupViewModel = new SignupViewModel();
        loginViewModel = new LoginViewModel();
        presenter = new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

        viewChanges = 0;
        signupNotifications = 0;
        loginNotifications = 0;
        viewManagerModel.addListener(() -> viewChanges++);
        signupViewModel.addListener(() -> signupNotifications++);
        loginViewModel.addListener(() -> loginNotifications++);
    }

    @Test
    void successPrefillsLoginUsername() {
        presenter.prepareSuccessView(new SignupOutputData("jonny", "Regular User"));

        assertEquals("jonny", loginViewModel.getState().getUsername());
        assertEquals(1, loginNotifications);
    }

    @Test
    void successGreetsNewRegularUser() {
        presenter.prepareSuccessView(new SignupOutputData("jonny", "Regular User"));

        assertEquals("Hello new Regular User! Log in to continue.",
                loginViewModel.getState().getWelcomeMessage());
    }

    @Test
    void successGreetsNewResearcher() {
        presenter.prepareSuccessView(new SignupOutputData("dr_smith", "Researcher"));

        assertEquals("Hello new Researcher! Log in to continue.",
                loginViewModel.getState().getWelcomeMessage());
    }

    @Test
    void successStartsLoginWithNoError() {
        loginViewModel.getState().setLoginError("old error");

        presenter.prepareSuccessView(new SignupOutputData("jonny", "Regular User"));

        assertEquals("", loginViewModel.getState().getLoginError());
    }

    @Test
    void successClearsSignupForm() {
        signupViewModel.getState().setUsername("jonny");
        signupViewModel.getState().setResearcher(true);
        signupViewModel.getState().setSignupError("old error");

        presenter.prepareSuccessView(new SignupOutputData("jonny", "Researcher"));

        assertEquals("", signupViewModel.getState().getUsername());
        assertFalse(signupViewModel.getState().isResearcher());
        assertEquals("", signupViewModel.getState().getSignupError());
        assertEquals(1, signupNotifications);
    }

    @Test
    void successSwitchesToLoginView() {
        presenter.prepareSuccessView(new SignupOutputData("jonny", "Regular User"));

        assertEquals(LoginViewModel.VIEW_NAME, viewManagerModel.getCurrentViewName());
        assertEquals(1, viewChanges);
    }

    @Test
    void failShowsErrorOnSignupState() {
        presenter.prepareFailView("Username already exists.");

        assertEquals("Username already exists.", signupViewModel.getState().getSignupError());
        assertEquals(1, signupNotifications);
    }

    @Test
    void failKeepsWhatTheUserTyped() {
        signupViewModel.getState().setUsername("jonny");
        signupViewModel.getState().setResearcher(true);

        presenter.prepareFailView("Username already exists.");

        assertEquals("jonny", signupViewModel.getState().getUsername());
        assertTrue(signupViewModel.getState().isResearcher());
    }

    @Test
    void failDoesNotChangeViewOrTouchLoginViewModel() {
        presenter.prepareFailView("nope");

        assertEquals("", viewManagerModel.getCurrentViewName());
        assertEquals(0, viewChanges);
        assertEquals(0, loginNotifications);
    }

    @Test
    void switchToLoginViewChangesView() {
        presenter.switchToLoginView();

        assertEquals(LoginViewModel.VIEW_NAME, viewManagerModel.getCurrentViewName());
        assertEquals(1, viewChanges);
        assertEquals(0, signupNotifications);
    }
}
