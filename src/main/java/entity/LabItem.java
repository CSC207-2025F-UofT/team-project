package entity;

/**
 * Anything on the bench. Vessels, pipettes, the dropper bottle, tools and fixed equipment
 * extend this.
 */
public abstract class LabItem {

  /** How close two items must be, in pixels, to count as near each other. */
  public static final double NEAR_DISTANCE = 60;

  private final String id;
  private final ItemType type;
  private Vec2 position;

  /**
   * Creates an item.
   *
   * @param id a unique id, such as "conical-1"
   * @param type what kind of item this is
   * @param position where it sits on the bench
   */
  protected LabItem(String id, ItemType type, Vec2 position) {
    this.id = id;
    this.type = type;
    this.position = position;
  }

  public String getId() {
    return id;
  }

  public ItemType getType() {
    return type;
  }

  public Vec2 getPosition() {
    return position;
  }

  public void setPosition(Vec2 position) {
    this.position = position;
  }

  /**
   * Whether another item is close enough to interact with this one.
   *
   * @param other the other item
   * @return true if the two are within {@link #NEAR_DISTANCE} of each other
   */
  public boolean isNear(LabItem other) {
    return Math.hypot(position.getX() - other.position.getX(),
        position.getY() - other.position.getY()) <= NEAR_DISTANCE;
  }
}