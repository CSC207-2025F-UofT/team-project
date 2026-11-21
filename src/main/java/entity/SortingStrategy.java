package entity;

public enum SortingStrategy {

  EASY("Easy"),
  AVERAGE("Average"),
  DIFFICULT("Difficult");

  private final String displayName;

  SortingStrategy(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}