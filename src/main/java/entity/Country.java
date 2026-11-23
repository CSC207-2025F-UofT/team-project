package entity;

public enum Country {
  CA("Canada"),
  US("USA"),
  ET("Ethiopia"),
  IN("India"),
  FR("France"),
  GH("Ghana");

  private final String displayName;

  Country(String displayName) {
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