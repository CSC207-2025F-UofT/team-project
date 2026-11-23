package use_case.manipulate_trips;

import java.util.List;

public class MergeTripsInputData {
    private final List<String> tripIds;

    public MergeTripsInputData(List<String> tripIds) {
        this.tripIds = List.copyOf(tripIds);
    }

    public List<String> getTripIds() { return tripIds; }
}