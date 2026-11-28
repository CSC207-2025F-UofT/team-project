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

    /**
     * Creates the interactor.
     * @param applicationDataAccess where applications are checked and saved
     * @param petDataAccess where pet availability and names are read
     * @param presenter the presenter for the result
     */
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
        final boolean available;
        final boolean alreadyApplied;
        try {
            available = petId != null && petDataAccess.isAvailable(petId);
            alreadyApplied = applicationDataAccess.hasActiveApplication(adopterId, petId);
        }
        catch (RuntimeException ex) {
            presenter.prepareFailView("Your application could not be checked. Please try again.");
            return;
        }
        if (!available) {
            presenter.prepareFailView("This pet is no longer available.");
            return;
        }
        if (alreadyApplied) {
            presenter.prepareFailView("You already have an application for this pet.");
            return;
        }

        // Read everything the success message needs before saving, so a failure here
        // never leaves a saved application that the adopter was not told about.
        final String petName;
        try {
            petName = petDataAccess.getPetName(petId);
        }
        catch (RuntimeException ex) {
            presenter.prepareFailView("The pet's details could not be loaded. Please try again.");
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

        presenter.prepareSuccessView(new SubmitApplicationOutputData(application.getId(), petName));
    }
}
