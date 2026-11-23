package use_case.signup;

import entities.RegularUser;
import entities.Researcher;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SignupInteractorTest {

    /** In-memory fake DAO so the test never touches Supabase. */
    private static class FakeDao implements SignupUserDataAccessInterface {
        final Map<String, User> users = new HashMap<>();
        final List<User> saved = new ArrayList<>();

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public void save(User user) {
            saved.add(user);
            users.put(user.getUsername(), user);
        }
    }

    /** Records which presenter method the interactor called. */
    private static class SpyPresenter implements SignupOutputBoundary {
        SignupOutputData success = null;
        String error = null;
        int calls = 0;
        boolean switchedToLogin = false;

        @Override
        public void prepareSuccessView(SignupOutputData outputData) {
            success = outputData;
            calls++;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
            calls++;
        }

        @Override
        public void switchToLoginView() {
            switchedToLogin = true;
        }
    }

    private FakeDao dao;
    private SpyPresenter presenter;
    private SignupInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new FakeDao();
        presenter = new SpyPresenter();
        interactor = new SignupInteractor(dao, presenter);
    }

    @Test
    void successRegularUser() {
        interactor.execute(new SignupInputData("jonny", false));

        assertNull(presenter.error);
        assertNotNull(presenter.success);
        assertEquals("jonny", presenter.success.getUsername());
        assertEquals("Regular User", presenter.success.getRole());
        assertEquals(1, presenter.calls);

        assertEquals(1, dao.saved.size());
        assertTrue(dao.saved.get(0) instanceof RegularUser);
        assertEquals("jonny", dao.saved.get(0).getUsername());
    }

    @Test
    void successResearcher() {
        interactor.execute(new SignupInputData("dr_smith", true));

        assertNull(presenter.error);
        assertNotNull(presenter.success);
        assertEquals("dr_smith", presenter.success.getUsername());
        assertEquals("Researcher", presenter.success.getRole());

        assertEquals(1, dao.saved.size());
        assertTrue(dao.saved.get(0) instanceof Researcher);
    }

    @Test
    void usernameIsTrimmedBeforeSaving() {
        interactor.execute(new SignupInputData("  jonny  ", false));

        assertEquals("jonny", presenter.success.getUsername());
        assertEquals("jonny", dao.saved.get(0).getUsername());
    }

    @Test
    void emptyUsernameFails() {
        interactor.execute(new SignupInputData("", false));

        assertNull(presenter.success);
        assertEquals("Username cannot be empty.", presenter.error);
        assertEquals(1, presenter.calls);
        assertTrue(dao.saved.isEmpty());
    }

    @Test
    void whitespaceOnlyUsernameFails() {
        interactor.execute(new SignupInputData("   ", true));

        assertNull(presenter.success);
        assertEquals("Username cannot be empty.", presenter.error);
        assertTrue(dao.saved.isEmpty());
    }

    @Test
    void nullUsernameFails() {
        interactor.execute(new SignupInputData(null, false));

        assertNull(presenter.success);
        assertEquals("Username cannot be empty.", presenter.error);
        assertTrue(dao.saved.isEmpty());
    }

    @Test
    void existingUsernameFailsAndDoesNotSave() {
        dao.users.put("jonny", new RegularUser("jonny"));

        interactor.execute(new SignupInputData("jonny", true));

        assertNull(presenter.success);
        assertEquals("Username already exists.", presenter.error);
        assertEquals(1, presenter.calls);
        assertTrue(dao.saved.isEmpty());
    }

    @Test
    void existingUsernameWithSurroundingWhitespaceStillFails() {
        dao.users.put("jonny", new RegularUser("jonny"));

        interactor.execute(new SignupInputData("  jonny ", false));

        assertEquals("Username already exists.", presenter.error);
        assertTrue(dao.saved.isEmpty());
    }

    @Test
    void switchToLoginViewDelegatesToPresenter() {
        interactor.switchToLoginView();

        assertTrue(presenter.switchedToLogin);
        assertEquals(0, presenter.calls);
    }
}