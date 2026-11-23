package use_case.add_deck;

public class AddDeckOutputData {

  // provide values to the presenter for updating into view model
  private final int deckId;
  private String deckName;
  private String owner;

  public AddDeckOutputData (int deckId, String deckName, String owner) {
    this.deckId = deckId;
    this.deckName = deckName;
    this.owner = owner;
  }

  public int getId() {
    return this.deckId;
  }

  public String getName() { return this.deckName; }

  public String getOwner() {
    return this.owner;
  }
}
