package use_case.add_deck;

import entity.Card;
import entity.User;

import java.util.ArrayList;

public class AddDeckOutputData {

  // provide values to the presenter for updating into view model
  private final int deckId;
  private String deckName;
  private String owner;
  private ArrayList<User> editors;
  private ArrayList<Card> cards;

  public AddDeckOutputData (int deckId, String deckName, String owner) {
    this.deckId = deckId;
    this.deckName = deckName;
    this.owner = owner;
    this.editors = new ArrayList<>();
    this.cards = new ArrayList<>();
  }

  public int getId() {
    return this.deckId;
  }

  public String getName() { return this.deckName; }

  public String getOwner() {
    return this.owner;
  }

  public ArrayList<User> getEditors() { return this.editors; }
}
