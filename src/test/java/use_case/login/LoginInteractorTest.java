package use_case.login;

import data_access.InMemoryUserDataAccessObject;
import data_access.Pbkdf2PasswordHasher;
import entity.User;
import entity.UserSession;
import org.junit.jupiter.api.Test;
import use_case.user.PasswordHasher;

import static org.junit.jupiter.api.Assertions.*;

public class LoginInteractorTest {
    @Test
    void loginSuccess(){
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        PasswordHasher hasher = new Pbkdf2PasswordHasher();

        dao.save(new User("Alex", hasher.hash("apple123")));
        LoginOutputBoundary presenter = new LoginOutputBoundary(){
            @Override
            public void prepareSuccessView(LoginOutputData outputData){
                assertEquals("Alex", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String errorMessage){
                fail("Login should succeed: " + errorMessage);
            }
        };

        UserSession session = new UserSession();
        LoginInteractor interactor = new LoginInteractor(dao, presenter, hasher, session);
        LoginInputData input = new LoginInputData("Alex", "apple123");

        interactor.execute(input);

        assertTrue(session.isLoggedIn());
        assertEquals("Alex", session.getCurrentUsername());
    }

    @Test
    void loginWrongPassword(){
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        PasswordHasher hasher = new Pbkdf2PasswordHasher();

        dao.save(new User("Alex", hasher.hash("apple123")));

        LoginOutputBoundary presenter = new LoginOutputBoundary(){
            @Override
            public void prepareSuccessView(LoginOutputData outputData){
                fail("Login should fail with wrong password");
            }

            @Override
            public void prepareFailView(String errorMessage){
                assertEquals("Invalid username or password", errorMessage);
            }
        };

        UserSession session = new UserSession();
        LoginInteractor interactor = new LoginInteractor(dao, presenter, hasher, session);
        LoginInputData input = new LoginInputData("Alex", "wrong123");

        interactor.execute(input);
    }

    @Test
    void loginUserNameNotFound(){
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        PasswordHasher hasher = new Pbkdf2PasswordHasher();
        LoginOutputBoundary presenter = new LoginOutputBoundary(){
            @Override
            public void prepareSuccessView(LoginOutputData outputData){
                fail("Login should fail when username does not exist");
            }

            @Override
            public void prepareFailView(String errorMessage){
                assertEquals("Invalid username or password", errorMessage);
            }
        };

        UserSession session = new UserSession();
        LoginInteractor interactor = new LoginInteractor(dao, presenter, hasher, session);
        LoginInputData input = new LoginInputData("Unknown", "apple123");

        interactor.execute(input);

        assertFalse(dao.existsByName("UnknownUser"));
    }
}
