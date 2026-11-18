package usecase.showcheckpoint;

import entity.Checkpoint;
import entity.QuizQuestion;
import java.util.List;
import java.util.Optional;
import usecase.CheckpointDataAccessInterface;
import usecase.ChoiceOrderer;

/**
 * Looks up the checkpoint that follows a step and hands its question to the presenter, with
 * the choices in the order the {@link ChoiceOrderer} picks each time it is shown.
 */
public class ShowCheckpointInteractor implements ShowCheckpointInputBoundary {
  private final CheckpointDataAccessInterface dataAccess;
  private final ShowCheckpointOutputBoundary presenter;
  private final ChoiceOrderer orderer;

  /**
   * Creates the interactor.
   *
   * @param dataAccess where the checkpoints are stored
   * @param presenter receives the question
   * @param orderer decides the order the choices are shown in
   */
  public ShowCheckpointInteractor(CheckpointDataAccessInterface dataAccess,
      ShowCheckpointOutputBoundary presenter, ChoiceOrderer orderer) {
    this.dataAccess = dataAccess;
    this.presenter = presenter;
    this.orderer = orderer;
  }

  @Override
  public void execute(ShowCheckpointInputData in) {
    Optional<Checkpoint> found = dataAccess.getCheckpoint(in.getStepIndex());
    if (found.isEmpty()) {
      presenter.prepareNoCheckpointView(in.getStepIndex());
      return;
    }
    Checkpoint checkpoint = found.get();
    QuizQuestion question = checkpoint.getQuestion();
    List<Integer> order = orderer.orderFor(question.getChoices().size());
    presenter.prepareSuccessView(new ShowCheckpointOutputData(checkpoint.getStepIndex(),
        checkpoint.getStepTitle(), question.getPrompt(),
        ChoiceOrderer.reorder(question.getChoices(), order), order,
        question.getType() == QuizQuestion.Type.MULTIPLE));
  }
}
