package use_case.logout;

import use_case.account.AuthSession;

import java.util.Optional;

public final class LogOutInteractor implements LogOutInputBoundary {
    private final LogOutSessionGateway sessions;
    private final SessionRevocationGateway auth;
    private final LogOutOutputBoundary output;

    public LogOutInteractor(LogOutSessionGateway sessions, SessionRevocationGateway auth,
                            LogOutOutputBoundary output) {
        this.sessions = sessions;
        this.auth = auth;
        this.output = output;
    }

    /**
     * Signs out locally before contacting the account service, so the user is signed out even if revocation fails
     * or is slow.
     */
    @Override
    public void execute() {
        final Optional<AuthSession> ended = sessions.endCurrentSession();
        output.signedOut();
        ended.ifPresent(auth::revoke);
    }
}
