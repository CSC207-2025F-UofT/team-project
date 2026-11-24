package use_case.logout;

import use_case.account.AuthSession;

import java.util.Optional;

public interface LogOutSessionGateway {
    /** Forgets the signed-in user and returns the session that was active, if any. */
    Optional<AuthSession> endCurrentSession();
}
