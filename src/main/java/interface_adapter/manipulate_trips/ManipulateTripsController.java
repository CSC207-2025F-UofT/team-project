package interface_adapter.manipulate_trips;

import java.util.List;

import use_case.manipulate_trips.ManipulateTripsInputBoundary;
import use_case.manipulate_trips.MergeTripsInputData;
import use_case.manipulate_trips.SplitTripInputData;
import use_case.manipulate_trips.ViewTripInputData;
import use_case.manipulate_trips.RenameTripInputData;

public class ManipulateTripsController {
    private final ManipulateTripsInputBoundary interactor;

    public ManipulateTripsController(ManipulateTripsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void listTrips() {
        interactor.listTrips();
    }

    public void viewTrip(String tripId) {
        interactor.viewTrip(new ViewTripInputData(tripId));
    }

    /** Moves firstPhotoId and every later photo of the trip into a new trip called newTripName. */
    public void splitTrip(String tripId, String firstPhotoId, String newTripName) {
        interactor.splitTrip(new SplitTripInputData(tripId, firstPhotoId, newTripName));
    }

    public void mergeTrips(List<String> tripIds) {
        interactor.mergeTrips(new MergeTripsInputData(tripIds));
    }

    public void renameTrip(String tripId, String newTripName) {
        interactor.renameTrip(new RenameTripInputData(tripId, newTripName));
    }
}