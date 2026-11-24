package use_case.adopter_signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import data_access.InMemoryAdopterDataAccessObject;
import data_access.Sha256PasswordHasher;
import entity.Adopter;
import use_case.PasswordHasher;

class AdopterSignupInteractorTest {
    private final InMemoryAdopterDataAccessObject dao = new InMemoryAdopterDataAccessObject();
    private final PasswordHasher hasher = new Sha256PasswordHasher();

    @Test
    void successCreatesAccountWithHashedPassword() {
        final AdopterSignupOutputBoundary presenter = new AdopterSignupOutputBoundary() {
            @Override
            public void prepareSuccessView(AdopterSignupOutputData outputData) {
                assertEquals("amy_01", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };
        new AdopterSignupInteractor(dao, presenter, hasher)
                .execute(new AdopterSignupInputData("  amy_01 ", "secret123", "secret123"));

        assertTrue(dao.existsByUsername("amy_01"));
        final Adopter saved = dao.get("amy_01");
        assertNotEquals("secret123", saved.getPasswordHash());
        assertTrue(hasher.matches("secret123", saved.getPasswordHash()));
    }

    @Test
    void failsWhenUsernameTaken() {
        dao.save(new Adopter("amy", hasher.hash("whatever1")));
        expectFailure("amy", "secret123", "secret123", "Username \"amy\" is already taken.");
    }

    @Test
    void failsWhenPasswordsDontMatch() {
        expectFailure("amy", "secret123", "secret124", "Passwords don't match.");
    }

    @Test
    void failsWhenPasswordTooShort() {
        expectFailure("amy", "123", "123", "Password must be at least 6 characters.");
    }

    @Test
    void failsWhenUsernameInvalid() {
        expectFailure("a,b", "secret123", "secret123",
                "Username must be 3-20 characters: letters, digits or _.");
    }

    @Test
    void failsWhenFieldsEmpty() {
        expectFailure("", "", "", "Please fill in username and password.");
        expectFailure(null, null, null, "Please fill in username and password.");
    }

    private void expectFailure(String username, String password, String repeat, String expectedError) {
        final AdopterSignupOutputBoundary presenter = new AdopterSignupOutputBoundary() {
            @Override
            public void prepareSuccessView(AdopterSignupOutputData outputData) {
                fail("Signup should have failed");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedError, errorMessage);
            }
        };
        final boolean existedBefore = username != null && dao.existsByUsername(username.trim());
        new AdopterSignupInteractor(dao, presenter, hasher)
                .execute(new AdopterSignupInputData(username, password, repeat));
        if (!existedBefore && username != null) {
            assertFalse(dao.existsByUsername(username.trim()));
        }
    }
}
