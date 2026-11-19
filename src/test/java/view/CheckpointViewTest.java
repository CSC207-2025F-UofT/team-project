package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import adapter.controller.AnswerCheckpointController;
import adapter.controller.ShowCheckpointController;
import adapter.presenter.AnswerCheckpointPresenter;
import adapter.presenter.ShowCheckpointPresenter;
import adapter.viewmodel.CheckpointViewModel;
import data.InMemoryLabDataAccessObject;
import data.RandomChoiceOrderer;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.AbstractButton;
import org.junit.jupiter.api.Test;
import usecase.answercheckpoint.AnswerCheckpointInteractor;
import usecase.showcheckpoint.ShowCheckpointInteractor;

class CheckpointViewTest {

  private static CheckpointView viewFor(CheckpointViewModel viewModel) {
    InMemoryLabDataAccessObject dataAccess = new InMemoryLabDataAccessObject();
    return new CheckpointView(viewModel,
        new ShowCheckpointController(
            new ShowCheckpointInteractor(dataAccess, new ShowCheckpointPresenter(viewModel),
                new RandomChoiceOrderer(new Random()))),
        new AnswerCheckpointController(
            new AnswerCheckpointInteractor(dataAccess, new AnswerCheckpointPresenter(viewModel))));
  }

  @Test
  void continueStaysLockedUntilTheAnswerIsCorrect() {
    CheckpointViewModel viewModel = new CheckpointViewModel();
    CheckpointView view = viewFor(viewModel);
    boolean[] continued = {false};
    view.open(0, () -> continued[0] = true);

    AbstractButton next = button(view, "I understand, continue →");
    assertFalse(next.isEnabled(), "locked before answering");

    // Wrong answer (original choice 1, wherever it is shown): still locked.
    shown(view, viewModel, 1).setSelected(true);
    button(view, "Check Answer").doClick();
    assertFalse(next.isEnabled());

    // Right answer (original choice 0, wherever it is shown): unlocked, and Continue moves on.
    shown(view, viewModel, 0).setSelected(true);
    button(view, "Check Answer").doClick();
    assertTrue(next.isEnabled());
    next.doClick();
    assertTrue(continued[0]);
  }

  @Test
  void checkingWithNothingSelectedShowsAMessageAndStaysLocked() {
    CheckpointViewModel viewModel = new CheckpointViewModel();
    CheckpointView view = viewFor(viewModel);
    view.open(0, () -> { });
    button(view, "Check Answer").doClick();
    assertEquals("Pick an answer first.", viewModel.getMessage());
    assertFalse(button(view, "I understand, continue →").isEnabled());
  }

  @Test
  void openingAnotherStepReplacesTheQuestionAndRelocksContinue() {
    CheckpointViewModel viewModel = new CheckpointViewModel();
    CheckpointView view = viewFor(viewModel);
    view.open(0, () -> { });
    shown(view, viewModel, 0).setSelected(true);
    button(view, "Check Answer").doClick();
    assertTrue(button(view, "I understand, continue →").isEnabled());

    view.open(1, () -> { });
    assertFalse(button(view, "I understand, continue →").isEnabled());
    assertTrue(button(view, "Check Answer").isEnabled());
  }

  @Test
  void aStepWithNoCheckpointContinuesStraightAway() {
    CheckpointViewModel viewModel = new CheckpointViewModel();
    CheckpointView view = viewFor(viewModel);
    boolean[] continued = {false};
    view.open(99, () -> continued[0] = true);
    assertTrue(continued[0], "the student is never stuck on a step without a checkpoint");
    assertTrue(viewModel.hasNoCheckpoint());
  }

  @Test
  void openingAStepWithACheckpointAfterOneWithoutShowsTheQuestion() {
    CheckpointViewModel viewModel = new CheckpointViewModel();
    CheckpointView view = viewFor(viewModel);
    view.open(99, () -> { });
    boolean[] continued = {false};
    view.open(0, () -> continued[0] = true);
    assertFalse(viewModel.hasNoCheckpoint());
    assertFalse(continued[0]);
    assertFalse(button(view, "I understand, continue →").isEnabled());
    assertEquals(4, choices(view).size());
  }

  @Test
  void theRightAnswerIsAcceptedInEveryShownPosition() {
    boolean[] seen = new boolean[4];
    for (int attempt = 0; attempt < 60 && !allTrue(seen); attempt++) {
      CheckpointViewModel viewModel = new CheckpointViewModel();
      CheckpointView view = viewFor(viewModel);
      view.open(0, () -> { });
      int position = viewModel.shownPositionOf(0);
      seen[position] = true;
      choices(view).get(position).setSelected(true);
      button(view, "Check Answer").doClick();
      assertTrue(button(view, "I understand, continue →").isEnabled(),
          "right answer shown at position " + position);
    }
    assertTrue(allTrue(seen), "the right answer moves around between showings");
  }

  @Test
  void aWrongAnswerIsRejectedInEveryShownPosition() {
    CheckpointViewModel viewModel = new CheckpointViewModel();
    CheckpointView view = viewFor(viewModel);
    view.open(0, () -> { });
    int rightPosition = viewModel.shownPositionOf(0);
    // Every other position is wrong, however the choices were shuffled.
    for (int position = 0; position < 4; position++) {
      if (position == rightPosition) {
        continue;
      }
      choices(view).get(position).setSelected(true);
      button(view, "Check Answer").doClick();
      assertFalse(button(view, "I understand, continue →").isEnabled(),
          "wrong answer at position " + position);
    }
  }

  private static boolean allTrue(boolean[] flags) {
    for (boolean flag : flags) {
      if (!flag) {
        return false;
      }
    }
    return true;
  }

  /** The button showing the choice whose original index is {@code original}. */
  private static AbstractButton shown(Container view, CheckpointViewModel viewModel,
      int original) {
    return choices(view).get(viewModel.shownPositionOf(original));
  }

  private static List<AbstractButton> choices(Container container) {
    List<AbstractButton> found = new ArrayList<>();
    collect(container, found);
    found.removeIf(button -> button.getText().startsWith("Check")
        || button.getText().startsWith("I understand"));
    return found;
  }

  private static AbstractButton button(Container container, String text) {
    List<AbstractButton> found = new ArrayList<>();
    collect(container, found);
    return found.stream().filter(button -> button.getText().equals(text)).findFirst()
        .orElseThrow(() -> new AssertionError("No button " + text));
  }

  private static void collect(Container container, List<AbstractButton> found) {
    for (Component child : container.getComponents()) {
      if (child instanceof AbstractButton) {
        found.add((AbstractButton) child);
      }
      if (child instanceof Container) {
        collect((Container) child, found);
      }
    }
  }
}
