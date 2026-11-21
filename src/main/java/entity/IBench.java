package entity;

import java.util.List;

/**
 * The table where the student's equipment sits.
 */
public interface IBench {

  /**
   * Puts an item on the bench.
   *
   * @param item the item to add
   * @param position where to place it
   */
  void addItem(LabItem item, Vec2 position);

  /**
   * Takes an item off the bench.
   *
   * @param id the id of the item to remove
   */
  void removeItem(String id);

  /**
   * Lists what is on the bench.
   *
   * @return a read-only list of the items
   */
  List<LabItem> getItems();
}