package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import usecase.ChoiceOrderer;

/**
 * Shows a question's choices in a fresh random order each time.
 */
public class RandomChoiceOrderer implements ChoiceOrderer {
  private final Random random;

  /**
   * Creates the orderer.
   *
   * @param random the source of randomness; seed it in tests for a repeatable order
   */
  public RandomChoiceOrderer(Random random) {
    this.random = random;
  }

  @Override
  public List<Integer> orderFor(int size) {
    List<Integer> order = new ArrayList<>(ChoiceOrderer.identity(size));
    Collections.shuffle(order, random);
    return List.copyOf(order);
  }
}
