package use_case.signIn;

import data_access.InMemoryUserDataAccessObject;
import entity.user.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignInInteractorTest {

    @Test
    void successTest() {
        // First test checks the case where the user logs in with the correct username and password
        SigninInputData inputData = new SigninInputData("goodUsername", "goodPassword");
        SignInUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // We add the goodUser to the data access repository before we log in.
        UserFactory factory = new UserFactory();
        // TODO Ask if we should be having a default token (so removed from factory, but included in entity)
        User goodUser = factory.create("goodUsername", "goodPassword");
        userRepository.save(goodUser);

        SignInOutputBoundary successPresenter = new SignInOutputBoundary() {
            @Override
            public void prepareSuccessView(SignInOutputData user) {
                assertEquals("goodUsername", user.getUsername());
                assertEquals("goodUsername", userRepository.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToHomePage() {
                // ignore this for the test
            }
        };

        SignInInputBoundary interactor = new SignInInteractor(successPresenter, userRepository);
        interactor.execute(inputData);
    }


    @Test
    void failurePasswordMismatchTest() {
        // Second test checks a correct username (exists) but with a wrong password
        SigninInputData inputData = new SigninInputData("goodUsername", "badPassword");
        SignInUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        UserFactory factory = new UserFactory();
        User user = factory.create("goodUsername", "goodPassword");
        userRepository.save(user);

        SignInOutputBoundary failurePresenter = new SignInOutputBoundary() {
            @Override
            public void prepareSuccessView(SignInOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect password for \"goodUsername\".", error);
            }

            @Override
            public void switchToHomePage() {
                // ignore this for the test
            }
        };

        SignInInputBoundary interactor = new SignInInteractor(failurePresenter, userRepository);
        interactor.execute(inputData);
    }

    @Test
    void failureUserDoesNotExistTest() {
        // Third test checks a username that doesn't exist (so the User doesn't exist)
        SigninInputData inputData = new SigninInputData("badUsername", "badPassword");
        SignInUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // This creates a presenter that tests whether the test case is as we expect.
        SignInOutputBoundary failurePresenter = new SignInOutputBoundary() {
            @Override
            public void prepareSuccessView(SignInOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("badUsername: Account does not exist.", error);
            }

            @Override
            public void switchToHomePage() {
                // ignore this for the test
            }
        };

        SignInInputBoundary interactor = new SignInInteractor(failurePresenter, userRepository);
        interactor.execute(inputData);
    }
}