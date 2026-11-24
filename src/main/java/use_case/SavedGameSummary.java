package use_case;

import java.time.Instant;
import java.util.UUID;

/** Metadata for the selection screen. The timestamp can be absent on older saves. */
public record SavedGameSummary(UUID runId, String name, int day, int energy, int sanity,
                               int academicSurvival, int money, Instant savedAt) {
    public static String defaultName(UUID runId) {
        return "Game " + runId.toString().substring(0, 8);
    }
}
