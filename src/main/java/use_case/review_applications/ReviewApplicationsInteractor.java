package use_case.review_applications;

import java.util.ArrayList;
import java.util.List;

import entity.Application;
import entity.ApplicationStatus;

/**
 * The Review Applications Interactor: lists applications for the admin, optionally filtered by pet and status,
 * and shows one chosen application with the adopter's answers.
 */
public class ReviewApplicationsInteractor implements ReviewApplicationsInputBoundary {

    /** Shown when there are no applications at all. */
    static final String NO_APPLICATIONS = "There are no applications.";
    /** Shown when there are applications, but none match the filters. */
    static final String NO_MATCHES = "No applications match the filter.";
    /** Shown when the applications cannot be read. */
    static final String LOAD_FAILED = "Applications could not be loaded. Please try again.";
    /** Shown when the admin opens an application without choosing one. */
    static final String NO_APPLICATION_CHOSEN = "Please choose an application.";
    /** Shown when the chosen application cannot be read. */
    static final String DETAIL_LOAD_FAILED = "Application could not be loaded. Please try again.";
    /** Shown when the chosen application no longer exists. */
    static final String APPLICATION_NOT_FOUND = "This application no longer exists.";
    /** Shown when the pet of an application can no longer be found, for example after it was removed. */
    static final String UNKNOWN_PET = "(pet no longer listed)";
    /** Shown when the adopter of an application can no longer be found. */
    static final String UNKNOWN_ADOPTER = "(unknown adopter)";

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
            presenter.prepareFailView(LOAD_FAILED);
            return;
        }
        if (applications.isEmpty()) {
            presenter.prepareFailView(NO_APPLICATIONS);
            return;
        }

        final String petId = inputData.getPetId();
        final ApplicationStatus status = inputData.getStatus();
        final List<ReviewApplicationSummary> summaries = new ArrayList<>();
        for (Application application : applications) {
            if (matches(application, petId, status)) {
                summaries.add(new ReviewApplicationSummary(application.getId(), application.getPetId(),
                        findPetName(application.getPetId()), findAdopterName(application.getAdopterId()),
                        application.getSubmittedAt(), application.getStatus().name()));
            }
        }

        if (summaries.isEmpty()) {
            presenter.prepareFailView(NO_MATCHES);
            return;
        }
        presenter.prepareSuccessView(new ReviewApplicationsOutputData(summaries));
    }

    @Override
    public void viewApplication(String applicationId) {
        if (applicationId == null || applicationId.isEmpty()) {
            presenter.prepareFailView(NO_APPLICATION_CHOSEN);
            return;
        }

        final Application application;
        try {
            application = applicationDataAccess.getApplicationById(applicationId);
        }
        catch (RuntimeException ex) {
            presenter.prepareFailView(DETAIL_LOAD_FAILED);
            return;
        }
        if (application == null) {
            presenter.prepareFailView(APPLICATION_NOT_FOUND);
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
            return UNKNOWN_PET;
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
            return UNKNOWN_ADOPTER;
        }
        return name;
    }
}
