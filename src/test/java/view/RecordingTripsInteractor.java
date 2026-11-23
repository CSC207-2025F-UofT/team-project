package view;

import use_case.manipulate_trips.ManipulateTripsInputBoundary;
import use_case.manipulate_trips.MergeTripsInputData;
import use_case.manipulate_trips.SplitTripInputData;
import use_case.manipulate_trips.ViewTripInputData;

/** Stand in for interactor in view tests: records what the controller forwarded. */
class RecordingTripsInteractor implements ManipulateTripsInputBoundary {
    int listCalls;
    ViewTripInputData viewed;
    SplitTripInputData split;
    MergeTripsInputData merged;

    @Override public void listTrips() { listCalls++; }
    @Override public void viewTrip(ViewTripInputData inputData) { viewed = inputData; }
    @Override public void splitTrip(SplitTripInputData inputData) { split = inputData; }
    @Override public void mergeTrips(MergeTripsInputData inputData) { merged = inputData; }
}