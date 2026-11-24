package data_access;

import entity.User;
import org.junit.jupiter.api.Test;
import use_case.account.AuthSession;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemorySessionStoreTest {
    private final InMemorySessionStore store = new InMemorySessionStore();
    private final UUID id = UUID.randomUUID();
    private final AuthSession session = new AuthSession(id, "token");
    private final User user = new User(id, "sam", "Sam");

    @Test
    void exposesTheSignedInUserAndTokenUntilTheSessionEnds() {
        store.start(session, user);
        assertEquals(Optional.of(user), store.currentUser());
        assertEquals(Optional.of("token"), store.accessToken());

        assertEquals(Optional.of(session), store.endCurrentSession());

        assertTrue(store.currentUser().isEmpty());
        assertTrue(store.accessToken().isEmpty());
        assertTrue(store.endCurrentSession().isEmpty());
    }

    @Test
    void refusesAProfileThatBelongsToAnotherUser() {
        final AuthSession otherSession = new AuthSession(UUID.randomUUID(), "token");

        assertThrows(IllegalArgumentException.class, () -> store.start(otherSession, user));
        assertTrue(store.currentUser().isEmpty());
    }
}
