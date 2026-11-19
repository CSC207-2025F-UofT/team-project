package usecase.estimateroi;

public interface EstimateROIOutputBoundary {

    void prepareSuccessView(EstimateROIOutputData outputData);

    void prepareFailView(String errorMessage);
}