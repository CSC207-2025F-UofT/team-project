package use_case.admin_signup;

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

class AdminSignupInteractorTest {
    private static final String INVITE_CODE = "shelter-2026";

    private final FakeUsers users = new FakeUsers();
    private final RecordingPresenter presenter = new RecordingPresenter();
    private final AdminSignupInteractor interactor =
            new AdminSignupInteractor(users, presenter, new UserFactory(), INVITE_CODE);

    private static AdminSignupInputData input(String id, String email, String password, String repeatPassword,
                                              String inviteCode) {
        return new AdminSignupInputData("Admin", id, email, password, repeatPassword, inviteCode);
    }

    @Test
    void savesAnAdminWhenTheInviteCodeIsValid() {
        interactor.execute(input("admin1", "admin@example.com", "password123", "password123", INVITE_CODE));

        assertEquals(1, users.saved.size());
        final User saved = users.saved.get(0);
        assertEquals("admin1", saved.getId());
        assertEquals(Role.ADMIN, saved.getRole());
        assertNull(saved.getPreference());
        assertNotEquals("password123", saved.getPasswordHash());
        assertEquals("admin@example.com", presenter.output.getEmail());
        assertNull(presenter.error);
    }

    @Test
    void rejectsAWrongOrMissingInviteCode() {
        interactor.execute(input("admin1", "admin@example.com", "password123", "password123", "guess"));
        assertEquals("Invalid admin invite code.", presenter.error);

        interactor.execute(input("admin1", "admin@example.com", "password123", "password123", null));
        assertEquals("Invalid admin invite code.", presenter.error);

        assertTrue(users.saved.isEmpty());
        assertNull(presenter.output);
    }

    @Test
    void rejectsEveryCodeWhenNoInviteCodeIsConfigured() {
        final AdminSignupInteractor locked = new AdminSignupInteractor(users, presenter, new UserFactory(), "");

        locked.execute(input("admin1", "admin@example.com", "password123", "password123", ""));

        assertEquals("Invalid admin invite code.", presenter.error);
        assertTrue(users.saved.isEmpty());
    }

    @Test
    void checksTheInviteCodeBeforeTheOtherFields() {
        interactor.execute(new AdminSignupInputData("", "", "", "", "", "wrong"));

        assertEquals("Invalid admin invite code.", presenter.error);
    }

    @Test
    void rejectsInvalidAccountDetails() {
        interactor.execute(new AdminSignupInputData("", "admin1", "admin@example.com", "password123",
                "password123", INVITE_CODE));
        assertEquals("Username cannot be empty", presenter.error);

        interactor.execute(input("", "admin@example.com", "password123", "password123", INVITE_CODE));
        assertEquals("Id cannot be empty", presenter.error);

        interactor.execute(input("admin1", "", "password123", "password123", INVITE_CODE));
        assertEquals("Email cannot be empty", presenter.error);

        interactor.execute(input("admin1", "admin.example.com", "password123", "password123", INVITE_CODE));
        assertEquals("Invalid email format", presenter.error);

        interactor.execute(input("admin1", "admin@example.com", "", "", INVITE_CODE));
        assertEquals("New password cannot be empty", presenter.error);

        interactor.execute(input("admin1", "admin@example.com", "short", "short", INVITE_CODE));
        assertEquals("Password must be at least 8 characters", presenter.error);

        interactor.execute(input("admin1", "admin@example.com", "password123", "different1", INVITE_CODE));
        assertEquals("Passwords don't match.", presenter.error);

        assertTrue(users.saved.isEmpty());
    }

    @Test
    void rejectsATakenIdOrEmail() {
        users.takenIds.add("admin1");
        interactor.execute(input("admin1", "admin@example.com", "password123", "password123", INVITE_CODE));
        assertEquals("User with this id already exists.", presenter.error);

        users.takenEmails.add("other@example.com");
        interactor.execute(input("admin2", "other@example.com", "password123", "password123", INVITE_CODE));
        assertEquals("Email is already registered.", presenter.error);

        assertTrue(users.saved.isEmpty());
    }

    @Test
    void switchesToTheLoginView() {
        interactor.switchToLoginView();

        assertTrue(presenter.switchedToLogin);
    }

    private static final class FakeUsers implements AdminSignupUserDataAccessInterface {
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

    private static final class RecordingPresenter implements AdminSignupOutputBoundary {
        private AdminSignupOutputData output;
        private String error;
        private boolean switchedToLogin;

        @Override
        public void prepareSuccessView(AdminSignupOutputData outputData) {
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
