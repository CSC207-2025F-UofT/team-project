package use_case.logout;

import org.junit.jupiter.api.Test;
import use_case.account.AuthSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogOutInteractorTest {
    private final List<String> events = new ArrayList<>();
    private final LogOutOutputBoundary presenter = () -> events.add("present signed out");
    private final SessionRevocationGateway revocation = session -> events.add("revoke " + session.accessToken());

    @Test
    void signsOutLocallyBeforeRevokingTheEndedSession() {
        final AuthSession session = new AuthSession(UUID.randomUUID(), "token");
        final LogOutSessionGateway sessions = () -> {
            events.add("end local session");
            return Optional.of(session);
        };

        new LogOutInteractor(sessions, revocation, presenter).execute();

        assertEquals(List.of("end local session", "present signed out", "revoke token"), events);
    }

    @Test
    void signingOutWithoutASessionSkipsRevocation() {
        new LogOutInteractor(Optional::empty, revocation, presenter).execute();

        assertEquals(List.of("present signed out"), events);
    }
}
