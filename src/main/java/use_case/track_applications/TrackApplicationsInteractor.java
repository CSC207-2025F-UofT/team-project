package use_case.track_applications;

import java.util.ArrayList;
import java.util.List;

import entity.Application;

/**
 * The Track Applications Interactor.
 */
public class TrackApplicationsInteractor implements TrackApplicationsInputBoundary {

    /** Shown when the pet of an application can no longer be found, for example after it was removed. */
    static final String UNKNOWN_PET = "(pet no longer listed)";

    private final TrackApplicationsDataAccessInterface applicationDataAccess;
    private final TrackApplicationsPetDataAccessInterface petDataAccess;
    private final TrackApplicationsOutputBoundary presenter;

    /**
     * Creates the interactor.
     * @param applicationDataAccess where the adopter's applications are read
     * @param petDataAccess where pet names are read
     * @param presenter the presenter for the result
     */
    public TrackApplicationsInteractor(TrackApplicationsDataAccessInterface applicationDataAccess,
                                       TrackApplicationsPetDataAccessInterface petDataAccess,
                                       TrackApplicationsOutputBoundary presenter) {
        this.applicationDataAccess = applicationDataAccess;
        this.petDataAccess = petDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(TrackApplicationsInputData inputData) {
        final String adopterId = inputData.getAdopterId();
        if (adopterId == null || adopterId.isEmpty()) {
            presenter.prepareFailView("Please log in to see your applications.");
            return;
        }

        final List<Application> applications;
        try {
            applications = applicationDataAccess.getApplicationsByAdopter(adopterId);
        }
        catch (RuntimeException ex) {
            presenter.prepareFailView("Your applications could not be loaded. Please try again.");
            return;
        }

        final List<ApplicationSummary> summaries = new ArrayList<>();
        for (Application application : applications) {
            summaries.add(new ApplicationSummary(application.getId(), findPetName(application.getPetId()),
                    application.getSubmittedAt(), application.getStatus().name(), application.getAdminNote()));
        }
        presenter.prepareSuccessView(new TrackApplicationsOutputData(summaries));
    }

    /**
     * Returns the pet's name, or a placeholder if the pet can no longer be found, so one missing pet
     * does not hide the adopter's other applications.
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
}
