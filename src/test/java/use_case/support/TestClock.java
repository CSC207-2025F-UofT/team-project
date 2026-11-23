package use_case.support;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

/** A clock frozen at a given time, so tests don't depend on the real time. */
public final class TestClock {

    private static final ZoneId ZONE = ZoneId.of("America/Toronto");

    private TestClock() {
    }

    public static Clock at(LocalDateTime time) {
        return Clock.fixed(time.atZone(ZONE).toInstant(), ZONE);
    }
}
