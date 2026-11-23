package use_case.manipulate_trips;

/** Implemented by presenter. Interactor reports results through this. */
public interface ManipulateTripsOutputBoundary {
    void prepareTripListView(ListTripsOutputData outputData);

    void prepareTripListFailView(String errorMessage);

    void prepareTripDetailsView(TripDetailsOutputData outputData);

    void prepareTripDetailsFailView(String errorMessage);

    void prepareSplitSuccessView(SplitTripOutputData outputData);

    void prepareSplitFailView(String errorMessage);

    void prepareMergeSuccessView(MergeTripsOutputData outputData);

    void prepareMergeFailView(String errorMessage);
}