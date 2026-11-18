package usecase;

import java.util.ArrayList;
import java.util.List;

/**
 * Decides the order a question's choices are shown in. The use cases depend on this interface,
 * not on any source of randomness, so how the order is picked can change (random, fixed for a
 * test, or something else) without touching them.
 *
 * <p>The question itself never changes: answers are always checked by the choice's original
 * index. A "choice order" tells the screen which original choice each shown position stands
 * for: element {@code p} is the original index of the choice shown at position {@code p}.
 */
public interface ChoiceOrderer {

  /** Shows every question's choices in their original order. */
  ChoiceOrderer IN_ORDER = ChoiceOrderer::identity;

  /**
   * Picks the order for one showing of a question.
   *
   * @param size how many choices the question has
   * @return a permutation of {@code 0..size-1}
   */
  List<Integer> orderFor(int size);

  /**
   * The order that leaves the choices as they are.
   *
   * @param size how many choices there are
   * @return {@code 0, 1, ..., size-1}
   */
  static List<Integer> identity(int size) {
    List<Integer> order = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      order.add(i);
    }
    return List.copyOf(order);
  }

  /**
   * Puts items into display order.
   *
   * @param items the items in their original order
   * @param order a choice order from {@link #orderFor}
   * @param <T> the item type
   * @return the items as they are shown: element {@code p} is {@code items.get(order.get(p))}
   */
  static <T> List<T> reorder(List<T> items, List<Integer> order) {
    List<T> shown = new ArrayList<>();
    for (int original : order) {
      shown.add(items.get(original));
    }
    return List.copyOf(shown);
  }
}
