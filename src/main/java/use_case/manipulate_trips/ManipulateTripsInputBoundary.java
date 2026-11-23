package use_case.manipulate_trips;

/** Entry point for Manipulate Trips use case: browsing and editing trips. */
public interface ManipulateTripsInputBoundary {

    /** Every trip, newest first. */
    void listTrips();

    /** One trip with its photos in chronological order. */
    void viewTrip(ViewTripInputData inputData);

    /**Moves a photo and every later photo of a trip into a new trip. */
    void splitTrip(SplitTripInputData inputData);

    /** Combines two or more trips into the earliest trip. */
    void mergeTrips(MergeTripsInputData inputData);
}