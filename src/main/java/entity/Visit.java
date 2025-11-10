package entity;

import java.time.Instant;
import java.util.List;

public class Visit {
    private final String visitId;
    private final Landmark landmark;
    private final Instant visitedAt;

    public Visit(String visitId,  Landmark landmark) {
        this.visitId = visitId;
        this.landmark = landmark;
        this.visitedAt =  Instant.now();
    }
    public String getVisitId() {
        return visitId;
    }
    public Landmark getLandmark() {
        return landmark;
    }
    public Instant getVisitedAt() {
        return visitedAt;
    }

}
