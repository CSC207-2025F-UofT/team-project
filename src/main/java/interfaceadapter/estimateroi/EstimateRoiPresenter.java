package interfaceadapter.estimateroi;

import usecase.estimateroi.EstimateRoiOutputBoundary;
import usecase.estimateroi.EstimateRoiOutputData;

/**
 * Updates the Estimate ROI view model when an estimate finishes.
 */
public class EstimateRoiPresenter implements EstimateRoiOutputBoundary {
  private final EstimateRoiViewModel viewModel;

  /**
   * Creates a presenter for Estimate ROI results.
   *
   * @param viewModel The view model to update with results and errors
   */
  public EstimateRoiPresenter(EstimateRoiViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void prepareSuccessView(EstimateRoiOutputData outputData) {
    viewModel.setState(new EstimateRoiState(outputData, null));
    viewModel.firePropertyChange(EstimateRoiViewModel.STATE_PROPERTY);
  }

  @Override
  public void prepareFailView(String errorMessage) {
    // Keep the previous estimate so that it stays unchanged on screen.
    viewModel.setState(new EstimateRoiState(viewModel.getState().getResult(), errorMessage));
    viewModel.firePropertyChange(EstimateRoiViewModel.STATE_PROPERTY);
  }
}
