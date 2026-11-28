package use_case.update_application_status;

public interface UpdateApplicationInputBoundary {

    void execute(UpdateApplicationInputData UpdateApplicationInputData);

    void switchToReviewApplicationView();
}
