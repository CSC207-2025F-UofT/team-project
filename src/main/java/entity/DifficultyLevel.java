package entity;

public enum DifficultyLevel {

  EASY("Easy"),
  AVERAGE("Average"),
  DIFFICULT("Difficult");

  private final String displayName;

  DifficultyLevel(String displayName) {
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