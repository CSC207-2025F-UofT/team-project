package entity;

/** The kinds of spending a traveller can log. */
public enum ExpenseCategory {
  FLIGHT("Flight"),
  TRANSPORT("Transport"),
  FOOD("Food"),
  LODGING("Lodging"),
  ACTIVITY("Activity"),
  SHOPPING("Shopping"),
  OTHER("Other");

  private final String displayName;

  ExpenseCategory(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Returns the name to show in the user interface.
   *
   * @return a human-readable name such as "Food"
   */
  public String getDisplayName() {
    return displayName;
  }
}
