package interface_adapter.Resubmit;

import usecase.Resubmit.ResubmitInputBoundary;
import usecase.Resubmit.ResubmitInputData;

import java.io.File;
import java.time.LocalTime;


public class ResubmitController {

    private final ResubmitInputBoundary resubmitUsecaseInteractor;

    public ResubmitController(ResubmitInputBoundary resubmitInputBoundary){
        this.resubmitUsecaseInteractor = resubmitInputBoundary;
    }

    public void resubmitExecute(LocalTime time, File selectedFile){
        ResubmitInputData inputData = new ResubmitInputData(time, selectedFile);
        resubmitUsecaseInteractor.execute(inputData);
    }
}
