package use_case.submit_application;

import entity.Application;

/**
 * DAO interface for the applications the Submit Application Use Case needs.
 */
public interface SubmitApplicationDataAccessInterface {

    /**
     * Checks whether the adopter already has an application for the pet that is still being considered.
     * @param adopterId the adopter
     * @param petId the pet
     * @return true if an active application exists
     */
    boolean hasActiveApplication(String adopterId, String petId);

    /**
     * Saves the application.
     * @param application the application to save
     */
    void save(Application application);
}
