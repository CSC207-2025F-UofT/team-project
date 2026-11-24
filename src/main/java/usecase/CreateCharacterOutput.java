package usecase;

/** Defines the output boundary for character creation. */
public interface CreateCharacterOutput {
  /** Presents the completed character data to the delivery mechanism. */
  void presentCreated(CreateCharacterResponse response);

  /** Presents a failure message when the requested character cannot be created. */
  void presentError(String message);
}
