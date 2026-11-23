package use_case.track_applications;

import java.util.List;

import entity.Application;

/**
 * DAO interface for the applications the Track Applications Use Case needs.
 */
public interface TrackApplicationsDataAccessInterface {

    /**
     * Returns every application submitted by the adopter.
     * @param adopterId the adopter
     * @return the adopter's applications, possibly empty
     */
    List<Application> getApplicationsByAdopter(String adopterId);
}
