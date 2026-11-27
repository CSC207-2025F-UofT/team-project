package use_case.update_application_status;

public interface UpdateApplicationOutputBoundary {

    void prepareSuccessView(UpdateApplicationOutputData outputData);

    void prepareFailView(String errorMessage);

    void switchToReviewApplicationView();
}
