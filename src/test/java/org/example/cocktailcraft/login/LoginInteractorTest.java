package org.example.cocktailcraft.login;

import org.example.cocktailcraft.data_access.InMemoryUserDataAccessObject;
import org.example.cocktailcraft.use_case.login.LoginInputData;
import org.example.cocktailcraft.use_case.login.LoginInteractor;
import org.example.cocktailcraft.use_case.login.LoginOutputBoundary;
import org.example.cocktailcraft.use_case.login.LoginOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LoginInteractorTest {

    @Test
    void successTest() {
        InMemoryUserDataAccessObject userDAO =
                new InMemoryUserDataAccessObject();

        userDAO.createUser("alice", "password123");

        LoginInputData inputData =
                new LoginInputData("alice", "password123");

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                assertEquals("alice", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Login should succeed: " + errorMessage);
            }
        };

        LoginInteractor interactor =
                new LoginInteractor(userDAO, presenter);

        interactor.execute(inputData);
    }

    @Test
    void incorrectPasswordTest() {
        assertLoginFailure("alice", "wrongpassword",
                "Invalid username or password.");
    }

    @Test
    void unknownUserTest() {
        assertLoginFailure("unknown", "password123",
                "Invalid username or password.");
    }

    @Test
    void emptyUsernameTest() {
        assertLoginFailure("", "password123",
                "Username cannot be empty.");
    }

    @Test
    void emptyPasswordTest() {
        assertLoginFailure("alice", "",
                "Password cannot be empty.");
    }

    private void assertLoginFailure(String username,
                                    String password,
                                    String expectedError) {
        InMemoryUserDataAccessObject userDAO =
                new InMemoryUserDataAccessObject();

        userDAO.createUser("alice", "password123");

        LoginInputData inputData =
                new LoginInputData(username, password);

        LoginOutputBoundary presenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData outputData) {
                fail("Login should fail.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        };

        LoginInteractor interactor =
                new LoginInteractor(userDAO, presenter);

        interactor.execute(inputData);
    }
}

