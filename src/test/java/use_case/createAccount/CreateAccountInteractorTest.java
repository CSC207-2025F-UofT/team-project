package use_case.createAccount;

import data_access.InMemoryUserDataAccessObject;
import entity.user.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAccountInteractorTest {

    @Test
    void successTest() {
        // First test checks that the user is created with valid inputs
        CreateAccountInputData inputData = new CreateAccountInputData("username", "password", "password");
        CreateAccountUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        CreateAccountOutputBoundary successPresenter = new CreateAccountOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateAccountOutputData user) {
                assertEquals("username", user.getUsername());
                assertTrue(userRepository.existsByName("username"));
            }

            @Override
            public void prepareFailView(String usernameError, String passwordError, String repeatPasswordError) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToHomePage() {
                // ignore this for the test
            }
        };

        CreateAccountInputBoundary interactor = new CreateAccountInteractor(successPresenter, userRepository, new UserFactory());
        interactor.execute(inputData);
    }


    @Test
    void failurePasswordMismatchTest() {
        // Second test checks that passwords that do not match can't create an account
        CreateAccountInputData inputData = new CreateAccountInputData("username", "password", "wrongPassword");
        CreateAccountUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        CreateAccountOutputBoundary failurePresenter = new CreateAccountOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateAccountOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String usernameError,
                                        String passwordError,
                                        String repeatPasswordError) {
                assertEquals("Passwords do not match", repeatPasswordError);
            }

            @Override
            public void switchToHomePage() {
                // ignore this for the test
            }
        };

        CreateAccountInputBoundary interactor = new CreateAccountInteractor(failurePresenter, userRepository, new UserFactory());
        interactor.execute(inputData);
    }


    @Test
    void failureUserExistsTest() {
        // Third test checks that if a user exists, they can't create a new account
        CreateAccountInputData inputData = new CreateAccountInputData("username", "password", "wrongPassword");
        CreateAccountUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // Add username to the repo so that they already exist
        UserFactory factory = new UserFactory();
        User user = factory.create("username", "password");
        userRepository.save(user);

        CreateAccountOutputBoundary failurePresenter = new CreateAccountOutputBoundary() {
            @Override
            public void prepareSuccessView(CreateAccountOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String usernameError,
                                        String passwordError,
                                        String repeatPasswordError) {
                assertEquals("Username already exists", usernameError);
            }

            @Override
            public void switchToHomePage() {
                // ignore this for the test
            }
        };

        CreateAccountInputBoundary interactor = new CreateAccountInteractor(failurePresenter, userRepository, new UserFactory());
        interactor.execute(inputData);
    }
}


