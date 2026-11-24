package use_case.login;

import use_case.account.AccountProfile;

public sealed interface LogInResponse {
    record Success(AccountProfile profile) implements LogInResponse { }

    enum Failure implements LogInResponse {
        BAD_CREDENTIALS,
        NO_USABLE_SESSION,
        RETRYABLE_FAILURE
    }
}
