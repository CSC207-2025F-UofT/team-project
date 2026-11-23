package use_case.signup;

import data_access.InMemoryUserDataAccessObject;
import data_access.Pbkdf2PasswordHasher;
import entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import use_case.user.PasswordHasher;

public class SignupInteractorTest {
    @Test
    void signupSuccess(){
        InMemoryUserDataAccessObject dao =  new InMemoryUserDataAccessObject();
        PasswordHasher hasher = new Pbkdf2PasswordHasher();
        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                assertEquals("Alex", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Signup should succeed: " + errorMessage);
            }
        };

        SignupInteractor interactor = new SignupInteractor(dao, presenter, hasher);
        SignupInputData input = new SignupInputData("Alex", "apple123", "apple123");
        interactor.execute(input);

        assertTrue(dao.existsByName("Alex"));
        assertNotEquals("apple123", dao.get("Alex").getPasswordHash());
        assertTrue(hasher.verify("apple123", dao.get("Alex").getPasswordHash()));
    }

    @Test
    void signupDuplicateUsername(){
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        PasswordHasher hasher = new Pbkdf2PasswordHasher();
        dao.save(new User("Alex", hasher.hash("apple123")));
        SignupOutputBoundary presenter = new SignupOutputBoundary(){
            @Override
            public void prepareSuccessView(SignupOutputData outputData){
                fail("Signup should fail for duplicate username");
            }
            @Override
            public void prepareFailView(String errorMessage){
                assertEquals("Username already exists", errorMessage);
            }
        };
        SignupInteractor interactor =new SignupInteractor(dao, presenter, hasher);
        SignupInputData input = new SignupInputData("Alex", "banana456", "banana456");
        interactor.execute(input);

        assertTrue(hasher.verify("apple123", dao.get("Alex").getPasswordHash()));
    }

    @Test
    void signupPasswordDoNotMatch(){
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        PasswordHasher hasher = new Pbkdf2PasswordHasher();
        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                    fail("Signup should fail when passwords do not match");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Passwords do not match", errorMessage);
            }
        };
        SignupInteractor interactor = new SignupInteractor(dao, presenter, hasher);
        SignupInputData input = new SignupInputData("Alex", "apple123", "banana456");
        interactor.execute(input);
        assertFalse(dao.existsByName("Alex"));
    }

    @Test
    void signupEmptyUsernam(){
        InMemoryUserDataAccessObject dao = new InMemoryUserDataAccessObject();
        PasswordHasher hasher = new Pbkdf2PasswordHasher();
        SignupOutputBoundary presenter = new SignupOutputBoundary(){
            @Override
            public void prepareSuccessView(SignupOutputData outputData){
                fail("Signup should fail when username is empty");
            }

            @Override
            public void prepareFailView(String errorMessage){
                assertEquals("Username cannot be empty", errorMessage);
            }
        };
        SignupInteractor interactor = new SignupInteractor(dao, presenter, hasher);
        SignupInputData input = new SignupInputData("   ", "apple123", "apple123");
        interactor.execute(input);
        assertFalse(dao.existsByName("   "));
    }
}
