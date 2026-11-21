package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The items currently on the student's bench.
 */
public class Bench implements BenchInterface {

  private final List<LabItem> items = new ArrayList<>();

  @Override
  public void addItem(LabItem item, Vec2 position) {
    item.setPosition(position);
    items.add(item);
  }

  @Override
  public void removeItem(String id) {
    items.removeIf(item -> item.getId().equals(id));
  }

  @Override
  public List<LabItem> getItems() {
    return List.copyOf(items);
  }
}