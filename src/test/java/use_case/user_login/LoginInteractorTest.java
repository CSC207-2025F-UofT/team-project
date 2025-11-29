package use_case.user_login;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginInteractorTest {

    private FakeUserLoginDAO fakeDAO;
    private FakeUserLoginPresenter fakePresenter;
    private UserLoginInteractor interactor;

    @BeforeEach
    void setUp() {
        fakeDAO = new FakeUserLoginDAO();
        fakePresenter = new FakeUserLoginPresenter();
        interactor = new UserLoginInteractor(fakeDAO, fakePresenter);
    }

    @Test
    void testUserDoesNotExist() {
        UserLoginInputData input = new UserLoginInputData("user", "unkonwn", "pwd");
        interactor.execute(input);

        assertEquals("unkonwn: Account does not exist.", fakePresenter.errorMessage);
        assertNull(fakePresenter.successUser);
    }

    @Test
    void testIncorrectPassword() {
        fakeDAO.save(new User("bob", "123", "user"));

        UserLoginInputData input = new UserLoginInputData("user", "bob", "wrongPwd");
        interactor.execute(input);

        assertEquals("Incorrect password for \"bob\".", fakePresenter.errorMessage);
    }

    @Test
    void testSuccessLogin() {
        fakeDAO.save(new User("alice", "pass123", "admin"));

        UserLoginInputData input = new UserLoginInputData("admin", "alice", "pass123");
        interactor.execute(input);

        assertEquals("alice", fakePresenter.successUser.getUsername());
        assertEquals("admin", fakePresenter.successUser.getUserType());
        assertEquals("alice", fakeDAO.currentUser);
    }

    @Test
    void testGoBack() {
        interactor.goBack();
        assertEquals("loginChoose", fakePresenter.goBackTarget);
    }

    // ====================================================
    //                      Fake DAO
    // ====================================================
    static class FakeUserLoginDAO implements UserLoginUserDataAccessInterface {
        private java.util.Map<String, User> users = new java.util.HashMap<>();
        String currentUser = null;

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public User get(String username) {
            return users.get(username);
        }

        @Override
        public void save(User user) {
            users.put(user.getName(), user);
        }

        @Override
        public void setCurrentUsername(String username) {
            this.currentUser = username;
        }
        @Override
        public String getCurrentUsername() {
            return currentUser;
        }
    }

    // ====================================================
    //                   Fake Presenter
    // ====================================================
    static class FakeUserLoginPresenter implements UserLoginOutputBoundary {

        UserLoginOutputData successUser = null;
        String errorMessage = null;
        String goBackTarget = null;

        @Override
        public void prepareSuccessView(UserLoginOutputData data) {
            this.successUser = data;
        }

        @Override
        public void prepareFailView(String error) {
            this.errorMessage = error;
        }

        @Override
        public void prepareGoBackView(String viewName) {
            this.goBackTarget = viewName;
        }
    }
}


