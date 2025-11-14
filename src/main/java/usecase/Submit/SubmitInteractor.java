package usecase.Submit;

import java.io.File;
import java.io.IOException;

public class SubmitInteractor implements SubmitInputBoundary {

    private final SubmitUserDataAccessInterface submitUserDataAccess;
    private final SubmitOutputBoundary submitOutputBoundary;

    public SubmitInteractor(SubmitUserDataAccessInterface submitUserDataAccess, SubmitOutputBoundary submitOutputBoundary) {
        this.submitUserDataAccess = submitUserDataAccess;
        this.submitOutputBoundary = submitOutputBoundary;
    }

    @Override
    public void execute(SubmitInputData inputData) {
        //For demo, we don't need to classify different user right? So just upload it


        File studentWork = inputData.getSelectedFile();
        try {
            submitUserDataAccess.submit(studentWork);
            SubmitOutputData outputData = new SubmitOutputData("Successfully submitted!");
            submitOutputBoundary.prepareSuccessView(outputData);
        } catch (IOException e) {
            SubmitOutputData outputData = new SubmitOutputData("Network Error! Please try again later.");
            submitOutputBoundary.prepareFailureView(outputData);
        }
    }
}
