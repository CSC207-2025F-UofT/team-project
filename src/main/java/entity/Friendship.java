package entity;

import java.time.Instant;
import java.util.UUID;

public class Friendship {
    private final UUID userA; // always the smaller ID
    private final UUID userB; // always the larger ID
    private final Instant since;

    // Accepts the two IDs in any order and stores them sorted.
    public Friendship(UUID first, UUID second, Instant since) {
        boolean ordered = first.compareTo(second) <= 0;
        this.userA = ordered ? first : second;
        this.userB = ordered ? second : first;
        this.since = since;
    }

    public UUID getUserA() { return userA; }
    public UUID getUserB() { return userB; }
    public Instant getSince() { return since; }
}
