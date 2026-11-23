package interfaceadapter.estimateroi;

import usecase.estimateroi.EstimateROIOutputBoundary;
import usecase.estimateroi.EstimateROIOutputData;

/** Puts Estimate ROI results into the view model. */
public class EstimateROIPresenter implements EstimateROIOutputBoundary {
    private final EstimateROIViewModel viewModel;

    public EstimateROIPresenter(EstimateROIViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(EstimateROIOutputData outputData) {
        viewModel.setState(new EstimateROIState(outputData, null));
        viewModel.firePropertyChange(EstimateROIViewModel.STATE_PROPERTY);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // keep the previous estimate so it stays "unchanged" (extension 3a)
        viewModel.setState(new EstimateROIState(viewModel.getState().getResult(), errorMessage));
        viewModel.firePropertyChange(EstimateROIViewModel.STATE_PROPERTY);
    }
}