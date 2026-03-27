package use_case.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import entity.UserFactory;

class LoginInteractorTest {

    @Test
    void successTest() {
        LoginInputData inputData = new LoginInputData("Paul", "password");
//        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Paul to the data access repository before we log in.
        UserFactory factory = new UserFactory();
//        User user = factory.create("Paul", "password");
//        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                assertEquals("Paul", user.getUsername());
//                assertEquals("Paul", userRepository.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToSignupView() {
                fail("Switching to signup view is unexpected in this test.");
            }
        };

//        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
//        interactor.execute(inputData);
    }


    @Test
    void failurePasswordMismatchTest() {
        LoginInputData inputData = new LoginInputData("Paul", "wrong");
//        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For this failure test, we need to add Paul to the data access repository before we log in, and
        // the passwords should not match.
        UserFactory factory = new UserFactory();
//        User user = factory.create("Paul", "password");
//        userRepository.save(user);

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect password for \"Paul\".", error);
            }

            @Override
            public void switchToSignupView() {
                fail("Switching to signup view is unexpected in this test.");
            }
        };

//        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
//        interactor.execute(inputData);
    }

    @Test
    void failureUserDoesNotExistTest() {
        LoginInputData inputData = new LoginInputData("Paul", "password");
//        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // Add Paul to the repo so that when we check later they already exist

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Paul: Account does not exist.", error);
            }

            @Override
            public void switchToSignupView() {
                fail("Switching to signup view is unexpected in this test.");
            }
        };

//        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
//        interactor.execute(inputData);
    }

    @Test
    void switchToSignupViewTest() {
        // Create a presenter that expects switchToSignupView to be called
        LoginOutputBoundary switchPresenter = new LoginOutputBoundary() {
            private boolean switchCalled = false;

            @Override
            public void prepareSuccessView(LoginOutputData user) {
                fail("Success view is unexpected in switch test.");
            }

            @Override
            public void prepareFailView(String error) {
                fail("Fail view is unexpected in switch test.");
            }

            @Override
            public void switchToSignupView() {
                switchCalled = true;
            }
        };
    }
}