package use_case.signup;

import data_access.CSVTransactionDAO;
import entity.UserFactory;
import entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupInteractorTest {

    @Test
    void successTest() {
        SignupInputData inputData = new SignupInputData("Paul", "StrongP@ss1", "StrongP@ss1");
    //    SignupUserDataAccessInterface userRepository = new CSVTransactionDAO();

        // This creates a successPresenter that tests whether the test case is as we expect.
        SignupOutputBoundary successPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                assertEquals("Paul", user.getUsername());
            //    assertTrue(userRepository.existsByName("Paul"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToLoginView() {
                // This is expected
            }
        };

    //    SignupInputBoundary interactor = new SignupInteractor(userRepository, successPresenter, new UserFactory());
    //    interactor.execute(inputData);
    }

    @Test
    void failurePasswordMismatchTest() {
        SignupInputData inputData = new SignupInputData("Paul", "StrongP@ss1", "WrongP@ss1");
        //SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // This creates a presenter that tests whether the test case is as we expect.
        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Passwords don't match.", error);
            }

            @Override
            public void switchToLoginView() {
                // This is expected
            }
        };

    //    SignupInputBoundary interactor = new SignupInteractor(userRepository, failurePresenter, new UserFactory());
    //    interactor.execute(inputData);
    }

    @Test
    void failureUserExistsTest() {
        SignupInputData inputData = new SignupInputData("Paul", "StrongP@ss1", "StrongP@ss1");
    //    SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // Add Paul to the repo so that when we check later they already exist
        UserFactory factory = new UserFactory();
    //    User user = factory.create("Paul", "pwd");
     //   userRepository.save(user);

        // This creates a presenter that tests whether the test case is as we expect.
        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("User already exists.", error);
            }

            @Override
            public void switchToLoginView() {
                // Not used in this test
            }
        };

    //    SignupInputBoundary interactor = new SignupInteractor(userRepository, failurePresenter, new UserFactory());
    //    interactor.execute(inputData);
    }

    @Test
    void failurePasswordTooShortMultipleErrorsTest() {
        // Too short and also missing uppercase, digit, symbol (but has lowercase, no spaces)
        SignupInputData inputData = new SignupInputData("Paul", "short", "short");
        //    SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertTrue(error.contains("Password must be at least 8 characters long."));
                assertTrue(error.contains("Password must contain at least one uppercase letter."));
                assertTrue(error.contains("Password must contain at least one digit."));
                assertTrue(error.contains("Password must contain at least one symbol"));
                // Has lowercase, so no lowercase error expected
                assertFalse(error.contains("Password must contain at least one lowercase letter."));
                assertFalse(error.contains("Password must not contain spaces."));
            }

            @Override
            public void switchToLoginView() {
                // Not used
            }
        };

        // SignupInputBoundary interactor =
        //         new SignupInteractor(userRepository, failurePresenter, new UserFactory());
        // interactor.execute(inputData);
    }

    @Test
    void failurePasswordOnlyLowercaseMultipleErrorsTest() {
        // Only lowercase, length >= 8, no spaces
        // Missing: uppercase, digit, symbol
        SignupInputData inputData = new SignupInputData("Paul", "onlylower", "onlylower");
        //    SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertFalse(error.contains("Password must be at least 8 characters long."));
                assertFalse(error.contains("Password must contain at least one lowercase letter."));
                assertFalse(error.contains("Password must not contain spaces."));

                assertTrue(error.contains("Password must contain at least one uppercase letter."));
                assertTrue(error.contains("Password must contain at least one digit."));
                assertTrue(error.contains("Password must contain at least one symbol"));
            }

            @Override
            public void switchToLoginView() {
                // Not used
            }
        };

        // SignupInputBoundary interactor =
        //         new SignupInteractor(userRepository, failurePresenter, new UserFactory());
        // interactor.execute(inputData);
    }

    @Test
    void failurePasswordOnlyUppercaseMultipleErrorsTest() {
        // Only uppercase, length >= 8, no spaces
        // Missing: lowercase, digit, symbol
        SignupInputData inputData = new SignupInputData("Paul", "ONLYUPPER", "ONLYUPPER");
        //    SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertFalse(error.contains("Password must be at least 8 characters long."));
                assertFalse(error.contains("Password must contain at least one uppercase letter."));
                assertFalse(error.contains("Password must not contain spaces."));

                assertTrue(error.contains("Password must contain at least one lowercase letter."));
                assertTrue(error.contains("Password must contain at least one digit."));
                assertTrue(error.contains("Password must contain at least one symbol"));
            }

            @Override
            public void switchToLoginView() {
                // Not used
            }
        };

        // SignupInputBoundary interactor =
        //         new SignupInteractor(userRepository, failurePresenter, new UserFactory());
        // interactor.execute(inputData);
    }

    @Test
    void failurePasswordMissingDigitMultipleErrorsTest() {
        // Has upper, lower, symbol, length >= 8, no spaces
        // Missing: digit
        SignupInputData inputData = new SignupInputData("Paul", "WeakP@ss", "WeakP@ss");
        //    SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertFalse(error.contains("Password must be at least 8 characters long."));
                assertFalse(error.contains("Password must contain at least one lowercase letter."));
                assertFalse(error.contains("Password must contain at least one uppercase letter."));
                assertFalse(error.contains("Password must contain at least one symbol"));
                assertFalse(error.contains("Password must not contain spaces."));

                assertTrue(error.contains("Password must contain at least one digit."));
            }

            @Override
            public void switchToLoginView() {
                // Not used
            }
        };

        // SignupInputBoundary interactor =
        //         new SignupInteractor(userRepository, failurePresenter, new UserFactory());
        // interactor.execute(inputData);
    }

    @Test
    void failurePasswordMissingSymbolMultipleErrorsTest() {
        // Has upper, lower, digit, length >= 8, no spaces
        // Missing: symbol
        SignupInputData inputData = new SignupInputData("Paul", "WeakPass1", "WeakPass1");
        //    SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertFalse(error.contains("Password must be at least 8 characters long."));
                assertFalse(error.contains("Password must contain at least one lowercase letter."));
                assertFalse(error.contains("Password must contain at least one uppercase letter."));
                assertFalse(error.contains("Password must contain at least one digit."));
                assertFalse(error.contains("Password must not contain spaces."));

                assertTrue(error.contains("Password must contain at least one symbol"));
            }

            @Override
            public void switchToLoginView() {
                // Not used
            }
        };

        // SignupInputBoundary interactor =
        //         new SignupInteractor(userRepository, failurePresenter, new UserFactory());
        // interactor.execute(inputData);
    }

    @Test
    void failurePasswordContainsSpacesMultipleErrorsTest() {
        // Contains spaces; also check other rules depending on exact content
        // Here we make it otherwise strong, so only the space error should appear.
        SignupInputData inputData = new SignupInputData("Paul", "Strong P@ss1", "Strong P@ss1");
        //    SignupUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertTrue(error.contains("Password must not contain spaces."));
                assertFalse(error.contains("Password must be at least 8 characters long."));
                assertFalse(error.contains("Password must contain at least one lowercase letter."));
                assertFalse(error.contains("Password must contain at least one uppercase letter."));
                assertFalse(error.contains("Password must contain at least one digit."));
                assertFalse(error.contains("Password must contain at least one symbol"));
            }

            @Override
            public void switchToLoginView() {
                // Not used
            }
        };

        // SignupInputBoundary interactor =
        //         new SignupInteractor(userRepository, failurePresenter, new UserFactory());
        // interactor.execute(inputData);
    }
}