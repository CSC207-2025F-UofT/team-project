package use_case.user_login;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Backend;

import static org.junit.jupiter.api.Assertions.*;

class LoginInteractorTest {

    private UserLoginInteractor interactor;
    private FakeUserLoginDAO userDAO;
    private FakeUserLoginPresenter presenter;
    private Backend backend;

    @BeforeEach
    void setUp() {
        userDAO = new FakeUserLoginDAO();
        presenter = new FakeUserLoginPresenter();
        backend = new Backend();

        interactor = new UserLoginInteractor(userDAO, presenter, backend);
    }

    /** ----------- Test 1: user does not exist ----------- */
    @Test
    void testUserDoesNotExist() {
        UserLoginInputData input = new UserLoginInputData("user", "abc", "123");

        interactor.execute(input);

        assertEquals("abc: Account does not exist.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    /** ----------- Test 2: userType mismatch ----------- */
    @Test
    void testUserTypeMismatch() {
        // dao mock contains "user1" with userType = "admin"
        User mockUser = new User(1, "user1", "", User.idType.PHOTO_CARD,
                0, "", "user1", "pass", "admin");
        userDAO.saveUser(mockUser);

        UserLoginInputData input = new UserLoginInputData("user", "user1", "pass");

        interactor.execute(input);

        assertEquals("Its not user account.", presenter.failMessage);
        assertNull(presenter.successData);
    }

    /** ----------- Test 3: wrong password ----------- */
    @Test
    void testWrongPassword() {
        User mockUser = new User(1, "user1", "", User.idType.PHOTO_CARD,
                0, "", "user1", "correctpwd", "user");
        userDAO.saveUser(mockUser);

        UserLoginInputData input = new UserLoginInputData("user", "user1", "wrong");

        interactor.execute(input);

        assertEquals("Incorrect password for \"user1\".", presenter.failMessage);
    }

    /** ----------- Test 4: successful login ----------- */
    @Test
    void testSuccessfulLogin() {
        User mockUser = new User(1, "user1", "", User.idType.PHOTO_CARD,
                0, "", "user1", "123", "user");
        userDAO.saveUser(mockUser);

        UserLoginInputData input = new UserLoginInputData("user", "user1", "123");

        interactor.execute(input);

        assertNull(presenter.failMessage);
        assertNotNull(presenter.successData);

        assertEquals("user1", presenter.successData.getUsername());
        assertEquals("user", presenter.successData.getUserType());

        // confirm backend receives current user
        assertEquals(mockUser.getUsername(), backend.getCurrentUser().getUsername());

        // confirm DAO sets current username
        assertEquals("user1", userDAO.currentUsername);
    }

    /** ----------- Test 5: goBack() ----------- */
    @Test
    void testGoBack() {
        interactor.goBack();
        assertEquals("loginChoose", presenter.goBackViewName);
    }
}
