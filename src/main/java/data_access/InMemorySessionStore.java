package data_access;

import entity.User;
import use_case.account.AuthSession;
import use_case.account.CurrentUserReader;
import use_case.account.SessionStarter;
import use_case.logout.LogOutSessionGateway;

import java.util.Optional;

/**
 * Holds the signed-in user for the lifetime of the app. Other Supabase DAOs read {@link #accessToken()} so their
 * requests run as the signed-in user, which lets row-level security apply.
 */
public final class InMemorySessionStore implements SessionStarter, LogOutSessionGateway, CurrentUserReader {
    private AuthSession session;
    private User user;

    @Override
    public synchronized void start(AuthSession newSession, User newUser) {
        if (!newSession.userId().equals(newUser.getUserID())) {
            throw new IllegalArgumentException("The profile does not belong to this session.");
        }
        session = newSession;
        user = newUser;
    }

    @Override
    public synchronized Optional<AuthSession> endCurrentSession() {
        final Optional<AuthSession> ended = Optional.ofNullable(session);
        session = null;
        user = null;
        return ended;
    }

    @Override
    public synchronized Optional<User> currentUser() {
        return Optional.ofNullable(user);
    }

    public synchronized Optional<String> accessToken() {
        return session == null ? Optional.empty() : Optional.of(session.accessToken());
    }
}
