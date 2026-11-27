package interface_adapter.update_application_status;

import entity.ApplicationStatus;
import use_case.update_application_status.UpdateApplicationInputBoundary;
import use_case.update_application_status.UpdateApplicationInputData;

public class UpdateApplicationController {
    private final UpdateApplicationInputBoundary updateApplicationInteractor;

    public UpdateApplicationController(UpdateApplicationInputBoundary updateApplicationInteractor) {
        this.updateApplicationInteractor = updateApplicationInteractor;
    }

    public void execute(String applicationId, String newStatus, String adminNote) {
        final UpdateApplicationInputData inputData = new UpdateApplicationInputData(applicationId,
                toStatus(newStatus), adminNote);
        updateApplicationInteractor.execute(inputData);
    }

    private static ApplicationStatus toStatus(String status) {
        if (status == null || status.isEmpty()) {
            return null;
        }
        return ApplicationStatus.valueOf(status);
    }
}
