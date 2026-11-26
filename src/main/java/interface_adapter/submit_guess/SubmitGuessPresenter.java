package interface_adapter.submit_guess;

import interface_adapter.ViewManagerModel;
import use_case.submit_guess.SubmitGuessOutputBoundary;
import use_case.submit_guess.SubmitGuessOutputData;

public class SubmitGuessPresenter implements SubmitGuessOutputBoundary {
  private final ViewManagerModel managerModel;
  private final SubmitGuessViewModel viewModel;

  public SubmitGuessPresenter(ViewManagerModel managerModel, SubmitGuessViewModel viewModel) {
    this.managerModel = managerModel;
    this.viewModel = viewModel;
  }

  @Override
  public void prepareFailView(String failure) {
    SubmitGuessState state = viewModel.getState();
    state.setFailure(failure);
    viewModel.notifyStateChanged();
  }

  @Override
  public void prepareSuccessView(SubmitGuessOutputData outputData) {
    SubmitGuessState state = viewModel.getState();
    state.setPreviouslyGuessed(outputData.getPreviouslyGuessed());
    state.setGuessesRemaining(outputData.getGuessesRemaining());
    state.setZoomStep(outputData.getZoomStep());
    state.setFailure(null);
    viewModel.notifyStateChanged();
  }
}
