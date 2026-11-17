package data_access;

import java.util.ArrayList;
import java.util.List;

import entity.Application;
import use_case.submit_application.SubmitApplicationDataAccessInterface;

/**
 * In-memory implementation of the DAO for storing applications. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryApplicationDataAccessObject implements SubmitApplicationDataAccessInterface {

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

    /**
     * Returns every application submitted by the adopter.
     * @param adopterId the adopter
     * @return the adopter's applications, possibly empty
     */
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
