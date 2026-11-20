package adapter;

import javax.swing.SwingUtilities;
import usecase.CreateCharacterOutput;
import usecase.CreateCharacterResponse;
import view.CharacterCreatorDisplay;

/** Presents character creation results through a Swing display. */
public final class SwingCreateCharacterPresenter implements CreateCharacterOutput {
  private final CharacterCreatorDisplay display;

  /** Creates a presenter that updates the given Swing display. */
  public SwingCreateCharacterPresenter(CharacterCreatorDisplay display) {
    if (display == null) {
      throw new IllegalArgumentException("Character creator display required");
    }
    this.display = display;
  }

  /** Schedules the created-character result to be displayed on Swing's event thread. */
  @Override
  public void presentCreated(CreateCharacterResponse response) {
    String characterResult =
        "<html><b>"
            + response.getRaceName()
            + " "
            + response.getClassName()
            + "</b><br>"
            + "STR "
            + response.getStrength()
            + " &nbsp; DEX "
            + response.getDexterity()
            + " &nbsp; CON "
            + response.getConstitution()
            + " &nbsp; INT "
            + response.getIntelligence()
            + " &nbsp; WIS "
            + response.getWisdom()
            + " &nbsp; CHA "
            + response.getCharisma()
            + "</html>";
    SwingUtilities.invokeLater(() -> display.showCharacterCreated(characterResult));
  }

  /** Schedules an error message to be displayed on Swing's event thread. */
  @Override
  public void presentError(String message) {
    SwingUtilities.invokeLater(() -> display.showError(message));
  }
}
