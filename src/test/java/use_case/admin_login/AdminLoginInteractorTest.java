package use_case.admin_login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import data_access.Sha256PasswordHasher;
import entity.Admin;
import use_case.PasswordHasher;

class AdminLoginInteractorTest {
    private final PasswordHasher hasher = new Sha256PasswordHasher();
    private final Admin admin = new Admin(hasher.hash("admin123"));
    private final AdminLoginDataAccessInterface dao = () -> admin;

    @Test
    void successWithCorrectPassword() {
        final boolean[] succeeded = {false};
        final AdminLoginOutputBoundary presenter = new AdminLoginOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                succeeded[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };
        new AdminLoginInteractor(dao, presenter, hasher).execute(new AdminLoginInputData("admin123"));
        assertTrue(succeeded[0]);
    }

    @Test
    void failsWithWrongPassword() {
        expectFailure("nope", "Incorrect admin password.");
    }

    @Test
    void failsWhenEmpty() {
        expectFailure("", "Please enter the admin password.");
    }

    private void expectFailure(String password, String expectedError) {
        final AdminLoginOutputBoundary presenter = new AdminLoginOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                fail("Admin login should have failed");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        };
        new AdminLoginInteractor(dao, presenter, hasher).execute(new AdminLoginInputData(password));
    }
}
