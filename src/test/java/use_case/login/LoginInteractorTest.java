package use_case.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.User;
import entity.User.Role;

class LoginInteractorTest {
    private FakeUsers users;
    private RecordingPresenter presenter;
    private LoginInteractor interactor;

    @BeforeEach
    void setUp() {
        users = new FakeUsers();
        users.add(new User("Alice", "alice", "alice@example.com", sha256("password123"), Role.ADOPTER, null));
        users.add(new User("Admin", "admin1", "admin@example.com", sha256("adminpass1"), Role.ADMIN, null));
        presenter = new RecordingPresenter();
        interactor = new LoginInteractor(users, presenter);
    }

    private static String sha256(String raw) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, hash));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Test
    void logsInAnAdopterWithTheRightPassword() {
        interactor.execute(new LoginInputData("alice@example.com", "password123"));

        assertEquals("alice", users.currentUserId);
        assertEquals("Alice", presenter.output.getUsername());
        assertEquals("alice", presenter.output.getId());
        assertEquals(Role.ADOPTER, presenter.output.getRole());
        assertNull(presenter.error);
    }

    @Test
    void reportsTheAdminRoleSoTheAdminHomeOpens() {
        interactor.execute(new LoginInputData("admin@example.com", "adminpass1"));

        assertEquals("admin1", users.currentUserId);
        assertEquals(Role.ADMIN, presenter.output.getRole());
    }

    @Test
    void rejectsEmptyEmailOrPassword() {
        interactor.execute(new LoginInputData("", "password123"));
        assertEquals("Email cannot be empty", presenter.error);

        interactor.execute(new LoginInputData("alice@example.com", ""));
        assertEquals("Password cannot be empty", presenter.error);

        assertNull(users.currentUserId);
        assertNull(presenter.output);
    }

    @Test
    void givesTheSameMessageForAWrongPasswordAndAnUnknownEmail() {
        interactor.execute(new LoginInputData("alice@example.com", "wrongpassword"));
        assertEquals("Invalid email or password.", presenter.error);

        interactor.execute(new LoginInputData("nobody@example.com", "password123"));
        assertEquals("Invalid email or password.", presenter.error);

        assertNull(users.currentUserId);
        assertNull(presenter.output);
    }

    private static final class FakeUsers implements LoginUserDataAccessInterface {
        private final Map<String, User> byEmail = new HashMap<>();
        private String currentUserId;

        void add(User user) {
            byEmail.put(user.getEmail(), user);
        }

        @Override
        public User getByEmail(String email) {
            return byEmail.get(email);
        }

        @Override
        public void setCurrentUserId(String id) {
            currentUserId = id;
        }
    }

    private static final class RecordingPresenter implements LoginOutputBoundary {
        private LoginOutputData output;
        private String error;

        @Override
        public void prepareSuccessView(LoginOutputData outputData) {
            output = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
