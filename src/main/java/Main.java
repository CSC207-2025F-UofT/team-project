import interface_adapter.create_character.CreateCharacterController;
import interface_adapter.Dnd5eCatalogGateway;
import interface_adapter.create_character.SwingCreateCharacterPresenter;
import javax.swing.SwingUtilities;
import use_case.create_character.CreateCharacterInteractor;
import view.create_character.SwingCharacterCreatorView;

/** Starts the Swing character creator application. */
public class Main {
  /** Launches the character creator. */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          Dnd5eCatalogGateway catalog = new Dnd5eCatalogGateway();
          SwingCharacterCreatorView view = new SwingCharacterCreatorView(catalog);
          SwingCreateCharacterPresenter presenter = new SwingCreateCharacterPresenter(view);
          CreateCharacterController controller =
              new CreateCharacterController(new CreateCharacterInteractor(catalog, presenter));
          view.setController(controller);
          view.showWindow();
        });
  }
}
