package use_case.adopter_login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.InMemoryAdopterDataAccessObject;
import data_access.Sha256PasswordHasher;
import entity.Adopter;
import use_case.PasswordHasher;

class AdopterLoginInteractorTest {
    private final InMemoryAdopterDataAccessObject dao = new InMemoryAdopterDataAccessObject();
    private final PasswordHasher hasher = new Sha256PasswordHasher();

    @BeforeEach
    void addAccount() {
        dao.save(new Adopter("amy", hasher.hash("secret123")));
    }

    @Test
    void successWithCorrectPassword() {
        final AdopterLoginOutputBoundary presenter = new AdopterLoginOutputBoundary() {
            @Override
            public void prepareSuccessView(AdopterLoginOutputData outputData) {
                assertEquals("amy", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };
        new AdopterLoginInteractor(dao, presenter, hasher)
                .execute(new AdopterLoginInputData("amy", "secret123"));
    }

    @Test
    void failsWithWrongPassword() {
        expectFailure("amy", "wrongpass", "Incorrect password.");
    }

    @Test
    void failsWhenAccountMissing() {
        expectFailure("bob", "secret123", "Account \"bob\" does not exist.");
    }

    @Test
    void failsWhenFieldsEmpty() {
        expectFailure("", "secret123", "Please enter username and password.");
        expectFailure("amy", "", "Please enter username and password.");
    }

    private void expectFailure(String username, String password, String expectedError) {
        final AdopterLoginOutputBoundary presenter = new AdopterLoginOutputBoundary() {
            @Override
            public void prepareSuccessView(AdopterLoginOutputData outputData) {
                fail("Login should have failed");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        };
        new AdopterLoginInteractor(dao, presenter, hasher)
                .execute(new AdopterLoginInputData(username, password));
    }
}
