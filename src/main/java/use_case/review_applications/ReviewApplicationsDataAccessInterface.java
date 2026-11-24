package use_case.review_applications;

import java.util.List;

import entity.Application;

/**
 * Where the Review Applications use case reads applications.
 */
public interface ReviewApplicationsDataAccessInterface {

    /**
     * Returns every application, from every adopter.
     * @return all applications, empty if there are none
     */
    List<Application> getAllApplications();

    /**
     * Returns one application.
     * @param applicationId the application's id
     * @return the application, or null if no application has this id
     */
    Application getApplicationById(String applicationId);
}
