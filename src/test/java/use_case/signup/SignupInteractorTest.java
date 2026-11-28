package use_case.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.User;
import entity.User.Role;
import entity.UserFactory;

class SignupInteractorTest {
    private final FakeUsers users = new FakeUsers();
    private final RecordingPresenter presenter = new RecordingPresenter();
    private final SignupInteractor interactor = new SignupInteractor(users, presenter, new UserFactory());

    private static SignupInputData input(String username, String id, String email, String password,
                                         String repeatPassword) {
        return new SignupInputData(username, id, email, password, repeatPassword);
    }

    @Test
    void savesANewAdopterWithAHashedPassword() {
        interactor.execute(input("Alice", "alice", "alice@example.com", "password123", "password123"));

        assertEquals(1, users.saved.size());
        final User saved = users.saved.get(0);
        assertEquals("Alice", saved.getName());
        assertEquals("alice", saved.getId());
        assertEquals(Role.ADOPTER, saved.getRole());
        assertNull(saved.getPreference());
        assertNotEquals("password123", saved.getPasswordHash());
        assertEquals(64, saved.getPasswordHash().length());
        assertEquals("alice@example.com", presenter.output.getEmail());
        assertNull(presenter.error);
    }

    @Test
    void rejectsMissingFields() {
        interactor.execute(input("", "alice", "alice@example.com", "password123", "password123"));
        assertEquals("Username cannot be empty", presenter.error);

        interactor.execute(input("Alice", "", "alice@example.com", "password123", "password123"));
        assertEquals("Id cannot be empty", presenter.error);

        interactor.execute(input("Alice", "alice", "", "password123", "password123"));
        assertEquals("Email cannot be empty", presenter.error);

        interactor.execute(input("Alice", "alice", "alice@example.com", "", ""));
        assertEquals("New password cannot be empty", presenter.error);

        assertTrue(users.saved.isEmpty());
        assertNull(presenter.output);
    }

    @Test
    void rejectsAnInvalidEmail() {
        interactor.execute(input("Alice", "alice", "not-an-email", "password123", "password123"));

        assertEquals("Invalid email format", presenter.error);
        assertTrue(users.saved.isEmpty());
    }

    @Test
    void rejectsAShortPassword() {
        interactor.execute(input("Alice", "alice", "alice@example.com", "short", "short"));

        assertEquals("Password must be at least 8 characters", presenter.error);
        assertTrue(users.saved.isEmpty());
    }

    @Test
    void rejectsPasswordsThatDoNotMatch() {
        interactor.execute(input("Alice", "alice", "alice@example.com", "password123", "password124"));

        assertEquals("Passwords don't match.", presenter.error);
        assertTrue(users.saved.isEmpty());
    }

    @Test
    void rejectsATakenIdOrEmail() {
        users.takenIds.add("alice");
        interactor.execute(input("Alice", "alice", "alice@example.com", "password123", "password123"));
        assertEquals("User with this id already exists.", presenter.error);

        users.takenEmails.add("bob@example.com");
        interactor.execute(input("Bob", "bob", "bob@example.com", "password123", "password123"));
        assertEquals("Email is already registered.", presenter.error);

        assertTrue(users.saved.isEmpty());
    }

    @Test
    void switchesToTheLoginView() {
        interactor.switchToLoginView();

        assertTrue(presenter.switchedToLogin);
    }

    private static final class FakeUsers implements SignupUserDataAccessInterface {
        private final List<String> takenIds = new ArrayList<>();
        private final List<String> takenEmails = new ArrayList<>();
        private final List<User> saved = new ArrayList<>();

        @Override
        public boolean existsById(String id) {
            return takenIds.contains(id);
        }

        @Override
        public boolean existsByEmail(String email) {
            return takenEmails.contains(email);
        }

        @Override
        public void save(User user) {
            saved.add(user);
        }
    }

    private static final class RecordingPresenter implements SignupOutputBoundary {
        private SignupOutputData output;
        private String error;
        private boolean switchedToLogin;

        @Override
        public void prepareSuccessView(SignupOutputData outputData) {
            output = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }

        @Override
        public void switchToLoginView() {
            switchedToLogin = true;
        }
    }
}
