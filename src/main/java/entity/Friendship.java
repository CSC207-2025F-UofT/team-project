package entity;

import java.time.Instant;
import java.util.UUID;

/**
 * A friendship between two users. The two IDs are always stored in sorted
 * order, so the same pair of users always gives the same friendship,
 * whichever user sent the request.
 */
public class Friendship {
    private final UUID userA; // always the smaller ID
    private final UUID userB; // always the larger ID
    private final Instant since;

    /**
     * Creates a friendship. The two IDs may be given in any order.
     *
     * @param first  the ID of one user
     * @param second the ID of the other user
     * @param since  when the two users became friends
     * @throws NullPointerException if {@code first} or {@code second} is {@code null}
     */
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
