package usecase.showcheckpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data.InMemoryLabDataAccessObject;
import data.RandomChoiceOrderer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import usecase.ChoiceOrderer;

class ShowCheckpointInteractorTest {
  private ShowCheckpointOutputData output;
  private Integer noCheckpointStep;
  private final InMemoryLabDataAccessObject dataAccess = new InMemoryLabDataAccessObject();
  private final ShowCheckpointOutputBoundary presenter = new ShowCheckpointOutputBoundary() {
    @Override
    public void prepareSuccessView(ShowCheckpointOutputData out) {
      output = out;
    }

    @Override
    public void prepareNoCheckpointView(int stepIndex) {
      noCheckpointStep = stepIndex;
    }
  };
  private final ShowCheckpointInteractor interactor =
      new ShowCheckpointInteractor(dataAccess, presenter, new RandomChoiceOrderer(new Random()));

  @Test
  void showsTheQuestionForAStep() {
    interactor.execute(new ShowCheckpointInputData(0));
    assertEquals(0, output.getStepIndex());
    assertEquals("Weigh the sample", output.getStepTitle());
    assertEquals(4, output.getChoices().size());
    assertFalse(output.isMultipleChoice());
  }

  @Test
  void tickAllQuestionsAreMarkedMultipleChoice() {
    interactor.execute(new ShowCheckpointInputData(3));
    assertTrue(output.isMultipleChoice());
  }

  @Test
  void choiceOrderMapsEveryShownChoiceBackToItsOriginal() {
    interactor.execute(new ShowCheckpointInputData(0));
    List<String> original = dataAccess.getCheckpoint(0).orElseThrow().getQuestion().getChoices();
    List<Integer> order = output.getChoiceOrder();
    assertEquals(new HashSet<>(List.of(0, 1, 2, 3)), new HashSet<>(order),
        "every choice appears exactly once");
    assertEquals(4, order.size());
    for (int shown = 0; shown < order.size(); shown++) {
      assertEquals(original.get(order.get(shown)), output.getChoices().get(shown));
    }
  }

  @Test
  void theOrderChangesFromOneShowingToTheNext() {
    ShowCheckpointInteractor seeded = new ShowCheckpointInteractor(dataAccess, presenter,
        new RandomChoiceOrderer(new Random(1)));
    List<List<Integer>> orders = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      seeded.execute(new ShowCheckpointInputData(0));
      orders.add(output.getChoiceOrder());
    }
    assertTrue(new HashSet<>(orders).size() > 1, "not the same order every time");
    assertTrue(orders.stream().anyMatch(order -> order.get(0) != 0),
        "the first choice doesn't always stay first");
  }

  @Test
  void sameSeedGivesTheSameOrder() {
    new ShowCheckpointInteractor(dataAccess, presenter,
        new RandomChoiceOrderer(new Random(7))).execute(new ShowCheckpointInputData(0));
    List<Integer> first = output.getChoiceOrder();
    new ShowCheckpointInteractor(dataAccess, presenter,
        new RandomChoiceOrderer(new Random(7))).execute(new ShowCheckpointInputData(0));
    assertEquals(first, output.getChoiceOrder());
  }

  @Test
  void anInOrderOrdererLeavesTheChoicesAsTheyAre() {
    new ShowCheckpointInteractor(dataAccess, presenter, ChoiceOrderer.IN_ORDER)
        .execute(new ShowCheckpointInputData(0));
    assertEquals(List.of(0, 1, 2, 3), output.getChoiceOrder());
    assertEquals(dataAccess.getCheckpoint(0).orElseThrow().getQuestion().getChoices(),
        output.getChoices());
  }

  @Test
  void aStepWithNoCheckpointIsReportedAsSuchNotAsAnError() {
    interactor.execute(new ShowCheckpointInputData(99));
    assertEquals(99, noCheckpointStep);
    assertNull(output, "no question is shown");
  }
}
