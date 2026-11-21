package entity;

public enum DifficultyLevel {
  BY_DUE_DATE("By Due Date"),
  BY_SUGGESTED_HOURS_TO_WORK_PER_DAY("By Suggested Hours To Work Per Day");

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