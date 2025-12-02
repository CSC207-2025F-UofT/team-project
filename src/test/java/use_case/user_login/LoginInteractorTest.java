package use_case.user_login;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for UserLoginInteractor ensuring 100% branch coverage.
 */
public class LoginInteractorTest {

    private UserLoginInteractor interactor;
    private FakeUserLoginDAO dao;
    private FakeUserLoginPresenter presenter;

    @BeforeEach
    void setUp() {
        dao = new FakeUserLoginDAO();
        presenter = new FakeUserLoginPresenter();
        interactor = new UserLoginInteractor(dao, presenter);
    }

    // === 1. Account does NOT exist ===
    @Test
    void testAccountDoesNotExist() {
        dao.exists = false;

        UserLoginInputData input =
                new UserLoginInputData("user", "Alice", "123");

        interactor.execute(input);

        assertEquals("Alice: Account does not exist.", presenter.failMessage);
    }

    // === 2. Wrong user type ===
    @Test
    void testWrongUserType() {
        dao.exists = true;

        // DB says this is "admin"
        dao.returnUser = new User(0, "Alice", "",
                User.idType.PHOTO_CARD, 0, "", "Alice", "123", "admin");

        // login page type = user, mismatch
        UserLoginInputData input =
                new UserLoginInputData("user", "Alice", "123");

        interactor.execute(input);

        assertEquals("Its not user account.", presenter.failMessage);
    }

    // === 3. Wrong password ===
    @Test
    void testWrongPassword() {
        dao.exists = true;

        // DB password = 999
        dao.returnUser = new User(0, "Bob", "",
                User.idType.PHOTO_CARD, 0, "", "Bob", "999", "user");

        UserLoginInputData input =
                new UserLoginInputData("user", "Bob", "123");

        interactor.execute(input);

        assertEquals("Incorrect password for \"Bob\".", presenter.failMessage);
    }

    // === 4. Login Success ===
    @Test
    void testLoginSuccess() {
        dao.exists = true;

        dao.returnUser = new User(0, "Chris", "",
                User.idType.PHOTO_CARD, 0, "", "Chris", "abc", "user");

        UserLoginInputData input =
                new UserLoginInputData("user", "Chris", "abc");

        interactor.execute(input);

        assertEquals("Chris", presenter.successUsername);
        assertEquals("user", presenter.successUserType);
        assertEquals("Chris", dao.currentUserSet);
    }

    // === 5. goBack ===
    @Test
    void testGoBack() {
        interactor.goBack();
        assertEquals("loginChoose", presenter.goBackView);
    }
}
