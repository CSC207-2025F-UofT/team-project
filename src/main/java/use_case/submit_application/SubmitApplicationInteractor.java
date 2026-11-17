package use_case.submit_application;

import java.time.LocalDate;
import java.util.UUID;

import entity.Application;

/**
 * The Submit Application Interactor.
 */
public class SubmitApplicationInteractor implements SubmitApplicationInputBoundary {

    private final SubmitApplicationDataAccessInterface applicationDataAccess;
    private final SubmitApplicationPetDataAccessInterface petDataAccess;
    private final SubmitApplicationOutputBoundary presenter;

    public SubmitApplicationInteractor(SubmitApplicationDataAccessInterface applicationDataAccess,
                                       SubmitApplicationPetDataAccessInterface petDataAccess,
                                       SubmitApplicationOutputBoundary presenter) {
        this.applicationDataAccess = applicationDataAccess;
        this.petDataAccess = petDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SubmitApplicationInputData inputData) {
        final String adopterId = inputData.getAdopterId();
        final String petId = inputData.getPetId();

        if (adopterId == null || adopterId.isEmpty()) {
            presenter.prepareFailView("Please log in before applying.");
            return;
        }
        if (petId == null || !petDataAccess.isAvailable(petId)) {
            presenter.prepareFailView("This pet is no longer available.");
            return;
        }
        if (applicationDataAccess.hasActiveApplication(adopterId, petId)) {
            presenter.prepareFailView("You already have an application for this pet.");
            return;
        }

        final Application application;
        try {
            application = new Application(UUID.randomUUID().toString(), adopterId, petId,
                    inputData.getHousingType(), inputData.getPetExperience(), inputData.getHouseholdMembers(),
                    inputData.getContactInfo(), inputData.getNote(), LocalDate.now());
        }
        catch (IllegalArgumentException ex) {
            presenter.prepareFailView(ex.getMessage());
            return;
        }

        try {
            applicationDataAccess.save(application);
        }
        catch (RuntimeException ex) {
            presenter.prepareFailView("The application could not be saved. Please try again.");
            return;
        }

        presenter.prepareSuccessView(
                new SubmitApplicationOutputData(application.getId(), petDataAccess.getPetName(petId)));
    }
}
