package org.accessgraph.use_case.plan_route;

import java.util.List;

/** The result of a successful search, as plain data the presenter can format. */
public final class PlanRouteOutputData {
    private final List<String> stationNames;
    private final List<String> legLines;
    private final int totalMinutes;
    private final int transfers;
    private final boolean stepFreeProfile;

    public PlanRouteOutputData(List<String> stationNames, List<String> legLines, int totalMinutes,
                               int transfers, boolean stepFreeProfile) {
        this.stationNames = List.copyOf(stationNames);
        this.legLines = List.copyOf(legLines);
        this.totalMinutes = totalMinutes;
        this.transfers = transfers;
        this.stepFreeProfile = stepFreeProfile;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public List<String> getLegLines() {
        return legLines;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int getTransfers() {
        return transfers;
    }

    public boolean isStepFreeProfile() {
        return stepFreeProfile;
    }
}
