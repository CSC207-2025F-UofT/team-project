package use_case.update_application_status;

import entity.Application;
import entity.ApplicationStatus;
import entity.Pet;
import entity.PetStatus;

public class UpdateApplicationInteractor implements UpdateApplicationInputBoundary {

    private final UpdateApplicationOutputBoundary presenter;
    private final UpdateApplicationPetDataAccessInterface petDataAccess;
    private final UpdateApplicationDataAccessInterface applicationDataAccess;

    public UpdateApplicationInteractor(UpdateApplicationOutputBoundary presenter,
                                       UpdateApplicationPetDataAccessInterface petDataAccess,
                                       UpdateApplicationDataAccessInterface applicationDataAccess) {
        this.presenter = presenter;
        this.applicationDataAccess = applicationDataAccess;
        this.petDataAccess = petDataAccess;
    }

    @Override
    public void execute(UpdateApplicationInputData inputData) {
        final ApplicationStatus status = inputData.getNewStatus();

        if (status != ApplicationStatus.APPROVED && status != ApplicationStatus.INTERVIEW
                && status != ApplicationStatus.REJECTED) {
            presenter.prepareFailView("Application status has to be one of "
                    + "APPROVED, INTERVIEW or REJECTED.");
            return;
        }

        final Application application = applicationDataAccess.getApplicationById(inputData.getApplicationId());
        if (application == null) {
            presenter.prepareFailView("Application not found");
            return;
        }

        application.updateStatus(status, inputData.getAdminNote());
        try {
            applicationDataAccess.save(application);
            if (status == ApplicationStatus.APPROVED) {
                Pet pet = petDataAccess.findById(application.getPetId());
                pet.updatePetStatus(PetStatus.ADOPTED);
                petDataAccess.update(pet);
            }
        }
        catch (RuntimeException ex) {
            presenter.prepareFailView("Could not save the change.");
            return;
        }

        presenter.prepareSuccessView(new UpdateApplicationOutputData(application.getId(),
                application.getStatus(), application.getAdminNote()));
    }

    @Override
    public void switchToReviewApplicationView() {
        presenter.switchToReviewApplicationView();
    }
}
