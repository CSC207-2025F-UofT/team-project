package use_case.signup;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for SignupInteractor with full branch/line coverage.
 */
class SignupInteractorTest {

    private SignupInteractor interactor;
    private FakeSignupDAO fakeDAO;
    private FakeSignupPresenter fakePresenter;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        fakeDAO = new FakeSignupDAO();
        fakePresenter = new FakeSignupPresenter();
        userFactory = new UserFactory();

        interactor = new SignupInteractor(fakeDAO, fakePresenter, userFactory);
    }

    @Test
    void testUserAlreadyExists() {
        fakeDAO.save(new User("john", "123", "user"));

        SignupInputData input = new SignupInputData("john", "123", "123");
        interactor.execute(input);

        assertEquals("User already exists.", fakePresenter.errorMessage);
        assertNull(fakePresenter.successUsername);
    }

    @Test
    void testPasswordsDontMatch() {
        SignupInputData input = new SignupInputData("abc", "123", "456");
        interactor.execute(input);

        assertEquals("Passwords don't match.", fakePresenter.errorMessage);
    }

    @Test
    void testPasswordEmpty() {
        SignupInputData input = new SignupInputData("abc", "", "");
        interactor.execute(input);

        assertEquals("New password cannot be empty", fakePresenter.errorMessage);
    }

    @Test
    void testUsernameEmpty() {
        SignupInputData input = new SignupInputData("", "123", "123");
        interactor.execute(input);

        assertEquals("Username cannot be empty", fakePresenter.errorMessage);
    }

    @Test
    void testSuccessSignup() {
        SignupInputData input = new SignupInputData("newUser", "123", "123");
        interactor.execute(input);

        assertEquals("newUser", fakePresenter.successUsername);
        assertTrue(fakeDAO.existsByName("newUser"));
    }

    @Test
    void testSwitchToLoginView() {
        interactor.switchToLoginView();
        assertTrue(fakePresenter.loginViewSwitched);
    }

    @Test
    void testGoBack() {
        interactor.goBack();
        assertEquals("loginChoose", fakePresenter.goBackTarget);
    }

    // ====== FAKE DAO ======
    static class FakeSignupDAO implements SignupUserDataAccessInterface {
        private final java.util.Map<String, User> users = new java.util.HashMap<>();

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public void save(User user) {
            users.put(user.getName(), user);
        }
    }

    // ====== FAKE PRESENTER ======
    static class FakeSignupPresenter implements SignupOutputBoundary {

        String errorMessage = null;
        String successUsername = null;

        boolean loginViewSwitched = false;
        String goBackTarget = null;

        @Override
        public void prepareFailView(String error) {
            this.errorMessage = error;
        }

        @Override
        public void prepareSuccessView(SignupOutputData data) {
            this.successUsername = data.getUsername();
        }

        @Override
        public void switchToLoginView() {
            loginViewSwitched = true;
        }

        @Override
        public void prepareGoBackView(String viewName) {
            this.goBackTarget = viewName;
        }
    }
}
