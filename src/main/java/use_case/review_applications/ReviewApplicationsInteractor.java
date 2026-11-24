package use_case.review_applications;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Application;
import entity.ApplicationStatus;

/**
 * The Review Applications Interactor: lists applications for the admin, optionally filtered by pet and status,
 * and shows one chosen application with the adopter's answers.
 */
public class ReviewApplicationsInteractor implements ReviewApplicationsInputBoundary {

    private final ReviewApplicationsDataAccessInterface applicationDataAccess;
    private final ReviewApplicationsPetDataAccessInterface petDataAccess;
    private final ReviewApplicationsUserDataAccessInterface userDataAccess;
    private final ReviewApplicationsOutputBoundary presenter;

    /**
     * Creates the interactor.
     * @param applicationDataAccess where applications are read
     * @param petDataAccess where pet names are read
     * @param userDataAccess where adopter names are read
     * @param presenter the presenter for the result
     */
    public ReviewApplicationsInteractor(ReviewApplicationsDataAccessInterface applicationDataAccess,
                                        ReviewApplicationsPetDataAccessInterface petDataAccess,
                                        ReviewApplicationsUserDataAccessInterface userDataAccess,
                                        ReviewApplicationsOutputBoundary presenter) {
        this.applicationDataAccess = applicationDataAccess;
        this.petDataAccess = petDataAccess;
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ReviewApplicationsInputData inputData) {
        final List<Application> applications;
        try {
            applications = applicationDataAccess.getAllApplications();
        }
        catch (RuntimeException ex) {
            presenter.prepareFailView("Applications could not be loaded. Please try again.");
            return;
        }
        if (applications.isEmpty()) {
            presenter.prepareFailView("There are no applications.");
            return;
        }

        final String petId = inputData.getPetId();
        final ApplicationStatus status = inputData.getStatus();
        final List<String> applicationIds = new ArrayList<>();
        final List<String> petNames = new ArrayList<>();
        final List<String> adopterNames = new ArrayList<>();
        final List<LocalDate> submittedDates = new ArrayList<>();
        final List<String> statuses = new ArrayList<>();
        for (Application application : applications) {
            if (matches(application, petId, status)) {
                applicationIds.add(application.getId());
                petNames.add(findPetName(application.getPetId()));
                adopterNames.add(findAdopterName(application.getAdopterId()));
                submittedDates.add(application.getSubmittedAt());
                statuses.add(application.getStatus().name());
            }
        }

        if (applicationIds.isEmpty()) {
            presenter.prepareFailView("No applications match the filter.");
            return;
        }
        presenter.prepareSuccessView(new ReviewApplicationsOutputData(applicationIds, petNames, adopterNames,
                submittedDates, statuses));
    }

    @Override
    public void viewApplication(String applicationId) {
        if (applicationId == null || applicationId.isEmpty()) {
            presenter.prepareDetailFailView("Please choose an application.");
            return;
        }

        final Application application;
        try {
            application = applicationDataAccess.getApplicationById(applicationId);
        }
        catch (RuntimeException ex) {
            presenter.prepareDetailFailView("Application could not be loaded. Please try again.");
            return;
        }
        if (application == null) {
            presenter.prepareDetailFailView("This application no longer exists.");
            return;
        }

        presenter.prepareDetailView(new ApplicationDetailsOutputData(application.getId(), application.getPetId(),
                findPetName(application.getPetId()), findAdopterName(application.getAdopterId()),
                application.getSubmittedAt(), application.getStatus().name(), application.getHousingType(),
                application.getPetExperience(), application.getHouseholdMembers(), application.getContactInfo(),
                application.getNote(), application.getAdminNote()));
    }

    /**
     * Returns whether an application passes the filters. An empty filter lets every application through.
     * @param application the application to check
     * @param petId the pet filter, or null or empty for every pet
     * @param status the status filter, or null for every status
     * @return true if the application should be shown
     */
    private static boolean matches(Application application, String petId, ApplicationStatus status) {
        final boolean petMatches = petId == null || petId.isEmpty() || petId.equals(application.getPetId());
        final boolean statusMatches = status == null || status == application.getStatus();
        return petMatches && statusMatches;
    }

    /**
     * Returns the pet's name, or a placeholder if the pet can no longer be found, so one missing pet
     * does not hide the other applications.
     * @param petId the pet
     * @return the pet's name, or the placeholder
     */
    private String findPetName(String petId) {
        try {
            return petDataAccess.getPetName(petId);
        }
        catch (RuntimeException ex) {
            return "(pet no longer listed)";
        }
    }

    /**
     * Returns the adopter's name, or a placeholder if the adopter can no longer be found.
     * @param adopterId the adopter
     * @return the adopter's name, or the placeholder
     */
    private String findAdopterName(String adopterId) {
        final String name = userDataAccess.getUserName(adopterId);
        if (name == null) {
            return "(unknown adopter)";
        }
        return name;
    }
}
