package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import usecase.ChoiceOrderer;

class RandomChoiceOrdererTest {

  @Test
  void ordersAreAlwaysAPermutation() {
    RandomChoiceOrderer orderer = new RandomChoiceOrderer(new Random(11));
    for (int i = 0; i < 50; i++) {
      List<Integer> order = orderer.orderFor(5);
      assertEquals(new HashSet<>(List.of(0, 1, 2, 3, 4)), new HashSet<>(order));
      assertEquals(5, order.size());
    }
  }

  @Test
  void theSameSeedGivesTheSameOrder() {
    assertEquals(new RandomChoiceOrderer(new Random(2)).orderFor(6),
        new RandomChoiceOrderer(new Random(2)).orderFor(6));
  }

  @Test
  void differentCallsGiveDifferentOrders() {
    RandomChoiceOrderer orderer = new RandomChoiceOrderer(new Random(4));
    boolean differs = false;
    List<Integer> first = orderer.orderFor(6);
    for (int i = 0; i < 20; i++) {
      differs |= !first.equals(orderer.orderFor(6));
    }
    assertTrue(differs);
  }

  @Test
  void aQuestionWithNoChoicesHasNoOrder() {
    assertEquals(List.of(), new RandomChoiceOrderer(new Random()).orderFor(0));
  }

  @Test
  void reorderAppliesTheOrderAndIdentityLeavesItAlone() {
    List<String> items = List.of("a", "b", "c");
    assertEquals(List.of("c", "a", "b"), ChoiceOrderer.reorder(items, List.of(2, 0, 1)));
    assertEquals(items, ChoiceOrderer.reorder(items, ChoiceOrderer.identity(3)));
    assertNotEquals(items, ChoiceOrderer.reorder(items, List.of(1, 0, 2)));
  }
}
