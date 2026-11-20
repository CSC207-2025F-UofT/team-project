package usecase;

/** Represents the race and character class selected by the user. */
public class CreateCharacterRequest {
  private final String raceName;
  private final String className;

  /** Creates a request containing the selected race and class names. */
  public CreateCharacterRequest(String raceName, String className) {
    this.raceName = raceName;
    this.className = className;
  }

  /** Returns the requested race name. */
  public String getRaceName() {
    return raceName;
  }

  /** Returns the requested character class name. */
  public String getClassName() {
    return className;
  }
}
