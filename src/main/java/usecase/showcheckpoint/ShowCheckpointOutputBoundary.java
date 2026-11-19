package usecase.showcheckpoint;

/**
 * Receives the checkpoint question to show.
 */
public interface ShowCheckpointOutputBoundary {

  /**
   * Shows the question.
   *
   * @param out the question to ask
   */
  void prepareSuccessView(ShowCheckpointOutputData out);

  /**
   * Reports that the step has no checkpoint, so there is nothing to pause for.
   *
   * @param stepIndex the step that was asked about
   */
  void prepareNoCheckpointView(int stepIndex);
}
