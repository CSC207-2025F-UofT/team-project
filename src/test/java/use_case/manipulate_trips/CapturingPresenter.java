package use_case.manipulate_trips;

/** Records what the interactor sent, instead of updating real ViewModels. Shared by the interactor tests. */
class CapturingPresenter implements ManipulateTripsOutputBoundary {
    ListTripsOutputData tripList;
    String tripListFailure;
    TripDetailsOutputData details;
    String detailsFailure;
    SplitTripOutputData split;
    String splitFailure;
    MergeTripsOutputData merge;
    String mergeFailure;

    @Override public void prepareTripListView(ListTripsOutputData outputData) { tripList = outputData; }
    @Override public void prepareTripListFailView(String errorMessage) { tripListFailure = errorMessage; }
    @Override public void prepareTripDetailsView(TripDetailsOutputData outputData) { details = outputData; }
    @Override public void prepareTripDetailsFailView(String errorMessage) { detailsFailure = errorMessage; }
    @Override public void prepareSplitSuccessView(SplitTripOutputData outputData) { split = outputData; }
    @Override public void prepareSplitFailView(String errorMessage) { splitFailure = errorMessage; }
    @Override public void prepareMergeSuccessView(MergeTripsOutputData outputData) { merge = outputData; }
    @Override public void prepareMergeFailView(String errorMessage) { mergeFailure = errorMessage; }
}