package use_case.update_application_status;

import use_case.login.LoginOutputData;

public interface UpdateApplicationOutputBoundary {

    void prepareSuccessView(UpdateApplicationOutputData outputData);

    void prepareFailView(String errorMessage);
}
