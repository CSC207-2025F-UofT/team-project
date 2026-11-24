package adapter;

import usecase.CreateCharacterInteractor;
import usecase.CreateCharacterRequest;

/** Converts character-selection input into a character-creation use-case request. */
public final class CreateCharacterController {
  private final CreateCharacterInteractor interactor;

  /** Creates the controller that converts UI input into a use-case request. */
  public CreateCharacterController(CreateCharacterInteractor interactor) {
    if (interactor == null) {
      throw new IllegalArgumentException("Interactor required");
    }
    this.interactor = interactor;
  }

  /** Passes the selected race and class to the character-creation use case. */
  public void createCharacter(String raceName, String className) {
    interactor.execute(new CreateCharacterRequest(raceName, className));
  }
}
