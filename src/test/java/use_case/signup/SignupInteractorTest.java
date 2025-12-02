package use_case.signup;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SignupInteractor ensuring 100% code coverage.
 */
public class SignupInteractorTest {

    private SignupInteractor interactor;
    private FakeSignupDAO dao;
    private FakeSignupPresenter presenter;
    private UserFactory factory;

    @BeforeEach
    void setUp() {
        dao = new FakeSignupDAO();
        presenter = new FakeSignupPresenter();
        factory = new UserFactory();
        interactor = new SignupInteractor(dao, presenter, factory);
    }

    // === 1. user already exists ===
    @Test
    void testUserAlreadyExists() {
        dao.exists = true;

        SignupInputData input = new SignupInputData("Alice", "pass", "pass");
        interactor.execute(input);

        assertEquals("User already exists.", presenter.failMessage);
    }

    // === 2. passwords don't match ===
    @Test
    void testPasswordMismatch() {
        dao.exists = false;

        SignupInputData input = new SignupInputData("Bob", "123", "456");
        interactor.execute(input);

        assertEquals("Passwords don't match.", presenter.failMessage);
    }

    // === 3. password empty ===
    @Test
    void testEmptyPassword() {
        dao.exists = false;

        SignupInputData input = new SignupInputData("Chris", "", "");
        interactor.execute(input);

        assertEquals("New password cannot be empty", presenter.failMessage);
    }

    // === 4. username empty ===
    @Test
    void testEmptyUsername() {
        dao.exists = false;

        SignupInputData input = new SignupInputData("", "123", "123");
        interactor.execute(input);

        assertEquals("Username cannot be empty", presenter.failMessage);
    }

    // === 5. success signup ===
    @Test
    void testSuccessSignup() {
        dao.exists = false;

        SignupInputData input = new SignupInputData("David", "abc", "abc");
        interactor.execute(input);

        assertEquals("David", presenter.successUsername);

        assertNotNull(dao.savedUser);
        assertEquals("David", dao.savedUser.getUsername());
        assertEquals("abc", dao.savedUser.getPassword());
        assertEquals("user", dao.savedUser.getUserType());
    }

    // === 6. switchToLoginView() ===
    @Test
    void testSwitchToLoginView() {
        interactor.switchToLoginView();
        assertTrue(presenter.loginViewSwitched);
    }

    // === 7. goBack() ===
    @Test
    void testGoBack() {
        interactor.goBack();
        assertEquals("loginChoose", presenter.goBackView);
    }
}
