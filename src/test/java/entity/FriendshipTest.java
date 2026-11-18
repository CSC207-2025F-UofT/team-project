package entity;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FriendshipTest {
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void storesUserIdsInCanonicalOrderRegardlessOfInputOrder(boolean reverseInput) {
        UUID smallerId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID largerId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        Instant since = Instant.parse("2026-11-17T12:00:00Z");

        Friendship friendship = reverseInput
                ? new Friendship(largerId, smallerId, since)
                : new Friendship(smallerId, largerId, since);

        assertEquals(smallerId, friendship.getUserA());
        assertEquals(largerId, friendship.getUserB());
        assertEquals(since, friendship.getSince());
    }
}
