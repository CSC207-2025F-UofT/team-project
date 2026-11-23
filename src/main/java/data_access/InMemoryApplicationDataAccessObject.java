package data_access;

import java.util.ArrayList;
import java.util.List;

import entity.Application;
import use_case.submit_application.SubmitApplicationDataAccessInterface;
import use_case.track_applications.TrackApplicationsDataAccessInterface;

/**
 * In-memory implementation of the DAO for storing applications. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryApplicationDataAccessObject implements SubmitApplicationDataAccessInterface,
        TrackApplicationsDataAccessInterface {

    private final List<Application> applications = new ArrayList<>();

    @Override
    public boolean hasActiveApplication(String adopterId, String petId) {
        for (Application application : applications) {
            if (application.getAdopterId().equals(adopterId) && application.getPetId().equals(petId)
                    && application.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Application application) {
        applications.add(application);
    }

    @Override
    public List<Application> getApplicationsByAdopter(String adopterId) {
        final List<Application> result = new ArrayList<>();
        for (Application application : applications) {
            if (application.getAdopterId().equals(adopterId)) {
                result.add(application);
            }
        }
        return result;
    }
}
