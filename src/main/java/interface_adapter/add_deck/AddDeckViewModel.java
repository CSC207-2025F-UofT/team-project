package interface_adapter.add_deck;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AddDeckViewModel {
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  private int deckId;
  private String name;
  private String owner;
  private String error;
  private String message;

  public void setSuccess(int deckId, String name, String owner, String message) {
    this.deckId = deckId;
    this.name = name;
    this.owner = owner;
    this.message = message;
    this.error = "";


    support.firePropertyChange("success deckAdded", null, null);
  }

  public void setError(String error) {
    this.error = error;
    this.message = "";

    support.firePropertyChange("error", null, error);
  }
}
