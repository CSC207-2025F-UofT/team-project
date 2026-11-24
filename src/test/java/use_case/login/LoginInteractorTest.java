package use_case.login;

import entities.RegularUser;
import entities.Researcher;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LoginInteractorTest {

    /** In-memory fake DAO so the test never touches Supabase. */
    private static class FakeDao implements LoginUserDataAccessInterface {
        final Map<String, User> users = new HashMap<>();

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public User get(String username) {
            return users.get(username);
        }
    }

    /** Records which presenter method the interactor called. */
    private static class SpyPresenter implements LoginOutputBoundary {
        LoginOutputData success = null;
        String error = null;
        int calls = 0;
        boolean switchedToSignup = false;

        @Override
        public void prepareSuccessView(LoginOutputData outputData) {
            success = outputData;
            calls++;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
            calls++;
        }

        @Override
        public void switchToSignupView() {
            switchedToSignup = true;
        }
    }

    private FakeDao dao;
    private SpyPresenter presenter;
    private LoginInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new FakeDao();
        presenter = new SpyPresenter();
        interactor = new LoginInteractor(dao, presenter);
    }

    @Test
    void successRegularUser() {
        dao.users.put("jonny", new RegularUser("jonny"));

        interactor.execute(new LoginInputData("jonny", false));

        assertNull(presenter.error);
        assertNotNull(presenter.success);
        assertEquals("jonny", presenter.success.getUsername());
        assertEquals("Regular User", presenter.success.getRole());
        assertEquals(1, presenter.calls);
    }

    @Test
    void successResearcher() {
        dao.users.put("dr_smith", new Researcher("dr_smith"));

        interactor.execute(new LoginInputData("dr_smith", true));

        assertNull(presenter.error);
        assertNotNull(presenter.success);
        assertEquals("dr_smith", presenter.success.getUsername());
        assertEquals("Researcher", presenter.success.getRole());
    }

    @Test
    void roleComesFromStoredUserNotFromTheResearcherFlag() {
        dao.users.put("jonny", new RegularUser("jonny"));

        interactor.execute(new LoginInputData("jonny", true));

        assertEquals("Regular User", presenter.success.getRole());
    }

    @Test
    void usernameWithSurroundingWhitespaceIsTrimmed() {
        dao.users.put("jonny", new RegularUser("jonny"));

        interactor.execute(new LoginInputData("  jonny  ", false));

        assertNull(presenter.error);
        assertNotNull(presenter.success);
        assertEquals("jonny", presenter.success.getUsername());
    }

    @Test
    void emptyUsernameFails() {
        interactor.execute(new LoginInputData("", false));

        assertNull(presenter.success);
        assertEquals("Username cannot be empty.", presenter.error);
        assertEquals(1, presenter.calls);
    }

    @Test
    void whitespaceOnlyUsernameFails() {
        interactor.execute(new LoginInputData("   ", false));

        assertNull(presenter.success);
        assertEquals("Username cannot be empty.", presenter.error);
    }

    @Test
    void nullUsernameFails() {
        interactor.execute(new LoginInputData(null, false));

        assertNull(presenter.success);
        assertEquals("Username cannot be empty.", presenter.error);
    }

    @Test
    void unknownUsernameFails() {
        interactor.execute(new LoginInputData("ghost", false));

        assertNull(presenter.success);
        assertEquals("Username doesn't exists. Please Sign up.", presenter.error);
        assertEquals(1, presenter.calls);
    }

    @Test
    void switchToSignupDelegatesToPresenter() {
        interactor.switchToSignupView();

        assertTrue(presenter.switchedToSignup);
        assertEquals(0, presenter.calls);
    }
}