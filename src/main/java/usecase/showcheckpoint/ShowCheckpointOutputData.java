package usecase.showcheckpoint;

import java.util.List;

/**
 * The checkpoint question, without its answer.
 */
public final class ShowCheckpointOutputData {
  private final int stepIndex;
  private final String stepTitle;
  private final String prompt;
  private final List<String> choices;
  private final List<Integer> choiceOrder;
  private final boolean multipleChoice;

  /**
   * Creates the output.
   *
   * @param stepIndex the step the checkpoint comes after
   * @param stepTitle that step's name
   * @param prompt the question text
   * @param choices the choice labels, in the order they are shown
   * @param choiceOrder for each shown position, the original index of the choice there; the
   *     index to report when the student picks that position
   * @param multipleChoice true if the student ticks every correct choice, false if they pick one
   */
  public ShowCheckpointOutputData(int stepIndex, String stepTitle, String prompt,
      List<String> choices, List<Integer> choiceOrder, boolean multipleChoice) {
    this.stepIndex = stepIndex;
    this.stepTitle = stepTitle;
    this.prompt = prompt;
    this.choices = choices;
    this.choiceOrder = choiceOrder;
    this.multipleChoice = multipleChoice;
  }

  public int getStepIndex() {
    return stepIndex;
  }

  public String getStepTitle() {
    return stepTitle;
  }

  public String getPrompt() {
    return prompt;
  }

  public List<String> getChoices() {
    return choices;
  }

  public List<Integer> getChoiceOrder() {
    return choiceOrder;
  }

  public boolean isMultipleChoice() {
    return multipleChoice;
  }
}
