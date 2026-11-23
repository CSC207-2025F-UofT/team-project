package interfaceadapter.estimateroi;

import usecase.estimateroi.EstimateROIInputBoundary;
import usecase.estimateroi.EstimateROIInputData;

/** Passes the user's request to the Estimate ROI interactor. */
public class EstimateROIController {
    private final EstimateROIInputBoundary interactor;

    public EstimateROIController(EstimateROIInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String ticker, double amount, int historyYears) {
        interactor.execute(new EstimateROIInputData(ticker, amount, historyYears));
    }
}