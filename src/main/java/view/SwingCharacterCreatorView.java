package view;

import adapter.CreateCharacterController;
import entity.CharacterClass;
import entity.Race;
import java.awt.GridLayout;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import usecase.CatalogGateway;

/** Creates the UI in Java Swing. */
public final class SwingCharacterCreatorView implements CharacterCreatorDisplay {
  private final CatalogGateway catalog;
  private final JFrame frame = new JFrame("Character Creator");
  private final JComboBox<Race> raceChoices = new JComboBox<>();
  private final JComboBox<CharacterClass> classChoices = new JComboBox<>();
  private final JButton createButton = new JButton("Create character");
  private final JLabel status = new JLabel("Loading races and classes from the D&D 5e API...");
  private final JLabel result = new JLabel("Choose a race and class for your new character.");
  private CreateCharacterController controller;

  /** Builds the character-creator window and retains the catalog provider. */
  public SwingCharacterCreatorView(CatalogGateway catalog) {
    if (catalog == null) {
      throw new IllegalArgumentException("Catalog gateway required");
    }
    this.catalog = catalog;
    buildWindow();
  }

  /** Supplies the controller used when the user requests character creation. */
  public void setController(CreateCharacterController controller) {
    if (controller == null) {
      throw new IllegalArgumentException("Character creator controller required");
    }
    this.controller = controller;
  }

  /** Shows the window and starts loading race and class options. */
  public void showWindow() {
    frame.setVisible(true);
    loadCatalogs();
  }

  /** Updates the result label with the created character's name and ability scores. */
  @Override
  public void showCharacterCreated(String characterResult) {
    result.setText(characterResult);
    status.setText("Character created.");
    createButton.setEnabled(true);
  }

  /** Shows an error in the status label and allows the user to retry. */
  @Override
  public void showError(String message) {
    status.setText(message);
    createButton.setEnabled(true);
  }

  /** Creates the basic form, result, and status controls in the frame. */
  private void buildWindow() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(new java.awt.Dimension(600, 450));
    frame.pack();
    frame.setLocationByPlatform(true);

    JPanel content = new JPanel(new GridLayout(0, 2, 8, 8));
    content.add(new JLabel("Race:"));
    raceChoices.setEnabled(false);
    classChoices.setEnabled(false);
    content.add(raceChoices);
    content.add(new JLabel("Class:"));
    content.add(classChoices);
    createButton.setEnabled(false);
    createButton.addActionListener(event -> createCharacter());
    content.add(new JLabel());
    content.add(createButton);
    content.add(new JLabel("Result:"));
    content.add(result);
    content.add(new JLabel("Status:"));
    content.add(status);
    frame.setContentPane(content);
  }

  /** Loads races and classes away from the Swing event thread. */
  private void loadCatalogs() {
    new SwingWorker<CatalogOptions, Void>() {
      /** Fetches catalog data without blocking the user interface. */
      @Override
      protected CatalogOptions doInBackground() {
        return new CatalogOptions(catalog.getRaces(), catalog.getClasses());
      }

      /** Populates selectors or reports the catalog-loading failure. */
      @Override
      protected void done() {
        try {
          CatalogOptions options = get();
          for (Race race : options.races) {
            raceChoices.addItem(race);
          }
          for (CharacterClass characterClass : options.classes) {
            classChoices.addItem(characterClass);
          }
          raceChoices.setEnabled(!options.races.isEmpty());
          classChoices.setEnabled(!options.classes.isEmpty());
          createButton.setEnabled(!options.races.isEmpty() && !options.classes.isEmpty());
          status.setText("Catalog loaded from the D&D 5e API.");
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          showError("Loading the D&D catalog was interrupted.");
        } catch (ExecutionException e) {
          Throwable cause = e.getCause();
          showError(
              "Could not load the D&D catalog: "
                  + (cause == null ? e.getMessage() : cause.getMessage()));
        }
      }
    }.execute();
  }

  /** Validates the selections and runs the creation request off the event thread. */
  private void createCharacter() {
    Race race = (Race) raceChoices.getSelectedItem();
    CharacterClass characterClass = (CharacterClass) classChoices.getSelectedItem();
    if (race == null || characterClass == null || controller == null) {
      showError("Select a race and a class.");
      return;
    }

    createButton.setEnabled(false);
    status.setText("Creating character...");
    new SwingWorker<Void, Void>() {
      /** Sends the selected race and class to the controller in the background. */
      @Override
      protected Void doInBackground() {
        controller.createCharacter(race.getName(), characterClass.getName());
        return null;
      }

      /** Handles completion and displays any request failure. */
      @Override
      protected void done() {
        try {
          get();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          showError("Character creation was interrupted.");
        } catch (ExecutionException e) {
          Throwable cause = e.getCause();
          showError(
              "Could not create character: "
                  + (cause == null ? e.getMessage() : cause.getMessage()));
        }
      }
    }.execute();
  }

  private static final class CatalogOptions {
    private final List<Race> races;
    private final List<CharacterClass> classes;

    /** Holds the race and class lists loaded for the selection controls. */
    private CatalogOptions(List<Race> races, List<CharacterClass> classes) {
      this.races = races;
      this.classes = classes;
    }
  }
}
