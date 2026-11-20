package org.accessgraph.interface_adapter.reroute;

import java.util.List;

/** What the disruption view shows: the active disruptions, and the result of the last change or an error. */
public final class RerouteState {
    /** An active disruption the passenger can choose to restore. */
    public static final class ActiveDisruption {
        private final String line;
        private final String stationId;
        private final String otherStationId;
        private final String label;

        public ActiveDisruption(String line, String stationId, String otherStationId, String label) {
            this.line = line;
            this.stationId = stationId;
            this.otherStationId = otherStationId;
            this.label = label;
        }

        public String getLine() {
            return line;
        }

        public String getStationId() {
            return stationId;
        }

        public String getOtherStationId() {
            return otherStationId;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private final List<ActiveDisruption> activeDisruptions;
    private final String headline;
    private final List<String> details;
    private final String error;

    public RerouteState(List<ActiveDisruption> activeDisruptions, String headline, List<String> details,
                        String error) {
        this.activeDisruptions = List.copyOf(activeDisruptions);
        this.headline = headline;
        this.details = List.copyOf(details);
        this.error = error;
    }

    public static RerouteState empty() {
        return new RerouteState(List.of(), "", List.of(), "");
    }

    public List<ActiveDisruption> getActiveDisruptions() {
        return activeDisruptions;
    }

    /** One sentence saying what was simulated or restored. */
    public String getHeadline() {
        return headline;
    }

    /** What the change means for the passenger's journey, one line of text per entry. */
    public List<String> getDetails() {
        return details;
    }

    public String getError() {
        return error;
    }
}
