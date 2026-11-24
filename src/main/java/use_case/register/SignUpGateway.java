package use_case.register;

import use_case.account.AuthSession;

import java.util.Objects;

public interface SignUpGateway {
    /**
     * Creates an account with the account service, which also stores the username and display name as the user's
     * profile. Does not change who is signed in.
     */
    Result signUp(String email, String password, String username, String displayName);

    sealed interface Result permits SignedUp, Failure { }

    record SignedUp(AuthSession session) implements Result {
        public SignedUp {
            Objects.requireNonNull(session);
        }
    }

    enum Failure implements Result {
        INVALID_EMAIL,
        PASSWORD_REJECTED,
        /** Rejected in a way that must not reveal whether the email is already registered. */
        ACCOUNT_UNAVAILABLE,
        /** The service refused to store the profile without saying why; a taken username is the usual cause. */
        PROFILE_REJECTED,
        /** The account was created but no session was issued, usually because email confirmation is required. */
        NO_USABLE_SESSION,
        /** The request may or may not have created an account. */
        SIGNUP_UNCERTAIN,
        RETRYABLE_FAILURE
    }
}
