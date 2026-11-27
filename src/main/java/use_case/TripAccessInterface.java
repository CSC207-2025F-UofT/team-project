package use_case;

import java.util.List;

import entity.Trip;

public interface TripAccessInterface {
    /**
     * Retrieves a trip by its identifier.
     *
     * @param id the trip identifier
     * @return the matching {@link Trip} or {@code null} if no trip with the given ID exists
     * @throws TripAccessException if accessing trip storage fails
     */
    Trip getTripById(String id);

    /**
     * Retrieves a list of all trips.
     *
     * @return the matching list of {@link Trip} entities
     * @throws TripAccessException if accessing trip storage fails
     */
    List<Trip> getTrips();

    /**
     * Writes a trip.
     *
     * @param trip the {@link Trip} being written
     * @throws TripAccessException if accessing trip storage fails
     */
    void writeTrip(Trip trip);

    /**
     * Writes a list of trips.
     *
     * @param trips the list of {@link Trip} entities being written
     * @throws TripAccessException if accessing trip storage fails
     */
    void writeTrips(List<Trip> trips);

    /**
     * Deletes a trip by its identifier.
     *
     * @param id the trip identifier
     * @throws TripAccessException if accessing trip storage fails
     */
    void deleteTripById(String id);
}
