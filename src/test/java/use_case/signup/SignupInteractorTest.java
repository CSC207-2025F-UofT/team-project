package use_case.signup;

import entity.User;
import entity.CommonUserFactory;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SignupInteractorTest {

    @Test
    public void testSignupInputData() {
        SignupInputData inputData =
                new SignupInputData(
                        "testuser",
                        "Test123456",
                        "Test123456"
                );

        assertEquals("testuser", inputData.getUsername());
        assertEquals("Test123456", inputData.getPassword());
        assertEquals("Test123456", inputData.getRepeatPassword());
    }
    private static class FakeUserDAO
            implements SignupUserDataAccessInterface {

        private final Map<String, User> users = new HashMap<>();

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public void save(User user) {
            users.put(user.getName(), user);
        }
    }

    private static class FakePresenter
            implements SignupOutputBoundary {

        private SignupOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(SignupOutputData data) {
            successData = data;
        }

        @Override
        public void prepareFailView(String error) {
            errorMessage = error;
        }
    }

    @Test
    public void testSignupSuccess() {
        FakeUserDAO dao = new FakeUserDAO();
        FakePresenter presenter = new FakePresenter();
        UserFactory factory = new CommonUserFactory();

        SignupInteractor interactor =
                new SignupInteractor(dao, presenter, factory);

        SignupInputData input =
                new SignupInputData("alice", "password123", "password123");

        interactor.execute(input);

        assertTrue(dao.existsByName("alice"));
        assertNotNull(presenter.successData);
        assertNull(presenter.errorMessage);
    }

    @Test
    public void testUsernameAlreadyExists() {
        FakeUserDAO dao = new FakeUserDAO();
        FakePresenter presenter = new FakePresenter();
        UserFactory factory = new CommonUserFactory();

        dao.save(factory.create("alice", "password123"));

        SignupInteractor interactor =
                new SignupInteractor(dao, presenter, factory);

        interactor.execute(
                new SignupInputData("alice", "password123", "password123")
        );

        assertEquals("Username already exists.", presenter.errorMessage);
        assertNull(presenter.successData);
    }

    @Test
    public void testPasswordsDoNotMatch() {
        FakeUserDAO dao = new FakeUserDAO();
        FakePresenter presenter = new FakePresenter();
        UserFactory factory = new CommonUserFactory();

        SignupInteractor interactor =
                new SignupInteractor(dao, presenter, factory);

        interactor.execute(
                new SignupInputData("bob", "password123", "different")
        );

        assertEquals("Passwords do not match.", presenter.errorMessage);
        assertFalse(dao.existsByName("bob"));
        assertNull(presenter.successData);
    }

    @Test
    public void testEmptyUsername() {
        FakeUserDAO dao = new FakeUserDAO();
        FakePresenter presenter = new FakePresenter();
        UserFactory factory = new CommonUserFactory();

        SignupInteractor interactor =
                new SignupInteractor(dao, presenter, factory);

        interactor.execute(
                new SignupInputData("", "password123", "password123")
        );

        assertEquals("Username cannot be empty.", presenter.errorMessage);
        assertFalse(dao.existsByName(""));
        assertNull(presenter.successData);
    }

    @Test
    public void testEmptyPassword() {
        FakeUserDAO dao = new FakeUserDAO();
        FakePresenter presenter = new FakePresenter();
        UserFactory factory = new CommonUserFactory();

        SignupInteractor interactor =
                new SignupInteractor(dao, presenter, factory);

        interactor.execute(
                new SignupInputData("bob", "", "")
        );

        assertEquals("Password cannot be empty.", presenter.errorMessage);
        assertFalse(dao.existsByName("bob"));
        assertNull(presenter.successData);
    }

}
