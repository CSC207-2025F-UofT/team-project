package use_case.login;

import use_case.account.AuthSession;

import java.util.Objects;

public interface SignInGateway {
    /** Verifies the credentials with the account service. Does not change who is signed in. */
    Result signIn(String email, String password);

    sealed interface Result permits SignedIn, Failure { }

    record SignedIn(AuthSession session) implements Result {
        public SignedIn {
            Objects.requireNonNull(session);
        }
    }

    enum Failure implements Result {
        /** Unknown email or wrong password. The two are intentionally indistinguishable. */
        BAD_CREDENTIALS,
        /** The credentials were accepted but no session was issued, usually because the email is unconfirmed. */
        NO_USABLE_SESSION,
        RETRYABLE_FAILURE
    }
}
