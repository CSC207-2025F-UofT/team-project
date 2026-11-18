package adapter.presenter;

import adapter.viewmodel.CheckpointViewModel;
import usecase.showcheckpoint.ShowCheckpointOutputBoundary;
import usecase.showcheckpoint.ShowCheckpointOutputData;

/**
 * Puts the checkpoint question on the checkpoint page.
 */
public class ShowCheckpointPresenter implements ShowCheckpointOutputBoundary {
  private final CheckpointViewModel viewModel;

  /**
   * Creates the presenter.
   *
   * @param viewModel the checkpoint page's view model
   */
  public ShowCheckpointPresenter(CheckpointViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void prepareSuccessView(ShowCheckpointOutputData out) {
    viewModel.setQuestion(out.getStepIndex(), out.getStepTitle(), out.getPrompt(),
        out.getChoices(), out.getChoiceOrder(), out.isMultipleChoice());
    viewModel.firePropertyChanged(CheckpointViewModel.QUESTION);
  }

  @Override
  public void prepareNoCheckpointView(int stepIndex) {
    viewModel.setNoCheckpoint(stepIndex);
    viewModel.firePropertyChanged(CheckpointViewModel.QUESTION);
  }
}
