package view;

/** Defines the user-interface operations used to display character creation results. */
public interface CharacterCreatorDisplay {
  /** Displays the formatted character result. */
  void showCharacterCreated(String characterResult);

  /** Displays an error produced while loading the catalog or creating a character. */
  void showError(String message);
}
