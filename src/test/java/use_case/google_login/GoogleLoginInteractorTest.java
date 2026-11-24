package use_case.google_login;

import data_access.InMemoryUserDataAccessObject;
import org.junit.jupiter.api.Test;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class GoogleLoginInteractorTest {
    private final InMemoryUserDataAccessObject accounts = new InMemoryUserDataAccessObject();

    @Test
    void successfulGoogleLoginSetsTheSharedSession() {
        GoogleLoginInteractor interactor = new GoogleLoginInteractor(() -> "123", accounts,
                new LoginOutputBoundary() {
                    @Override
                    public void prepareSuccessView(LoginOutputData response) {
                        assertEquals("google:123", response.getUsername());
                    }

                    @Override
                    public void prepareFailView(String error) {
                        fail(error);
                    }
                });

        interactor.execute();
        assertEquals("google:123", accounts.getCurrentUsername());
    }

    @Test
    void cancelledGoogleLoginLeavesSessionEmpty() {
        GoogleLoginInteractor interactor = new GoogleLoginInteractor(() -> {
            throw new IOException("Google sign-in was cancelled.");
        }, accounts, new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData response) {
                fail("A cancelled sign-in cannot start a session.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Google sign-in was cancelled.", error);
            }
        });

        interactor.execute();
        assertNull(accounts.getCurrentUsername());
    }
}
