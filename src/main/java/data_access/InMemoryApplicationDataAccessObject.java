package data_access;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import entity.Application;
import use_case.submit_application.SubmitApplicationDataAccessInterface;
import use_case.track_applications.TrackApplicationsDataAccessInterface;

/**
 * In-memory implementation of the DAO for storing applications. This implementation does
 * NOT persist data between runs of the program.
 * Saving an application replaces the saved one with the same id. Changes to an application
 * are stored only when it is saved.
 */
public class InMemoryApplicationDataAccessObject implements SubmitApplicationDataAccessInterface,
        TrackApplicationsDataAccessInterface {

    private final Map<String, Application> applications = new LinkedHashMap<>();

    @Override
    public boolean hasActiveApplication(String adopterId, String petId) {
        for (Application application : applications.values()) {
            if (application.getAdopterId().equals(adopterId) && application.getPetId().equals(petId)
                    && application.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Application application) {
        applications.put(application.getId(), application.copy());
    }

    @Override
    public List<Application> getApplicationsByAdopter(String adopterId) {
        final List<Application> result = new ArrayList<>();
        for (Application application : applications.values()) {
            if (application.getAdopterId().equals(adopterId)) {
                result.add(application.copy());
            }
        }
        return result;
    }
}
