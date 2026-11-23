package interfaceadapter.estimateroi;

import usecase.estimateroi.EstimateROIOutputData;

/** What the Estimate ROI panel shows. */
public class EstimateROIState {
    private final EstimateROIOutputData result;   // last good estimate (kept on error, per 3a)
    private final String errorMessage;

    EstimateROIState(EstimateROIOutputData result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public EstimateROIOutputData getResult() { return result; }
    public String getErrorMessage() { return errorMessage; }
}