package org.example.cocktailcraft.signup;

import org.example.cocktailcraft.data_access.InMemoryUserDataAccessObject;
import org.example.cocktailcraft.use_case.signup.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupInteractorTest {

    @Test
    void successTest() {
        InMemoryUserDataAccessObject userDAO =
                new InMemoryUserDataAccessObject();

        SignupInputData inputData =
                new SignupInputData("alice", "password123", "password123");

        SignupOutputBoundary presenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData outputData) {
                assertEquals("alice", outputData.getUsername());
                assertTrue(userDAO.existsByName("alice"));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Signup should succeed: " + errorMessage);
            }
        };

        SignupInteractor interactor = new SignupInteractor(userDAO, presenter);

        interactor.execute(inputData);

        assertTrue(userDAO.existsByName("alice"));
    }
}

