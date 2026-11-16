package use_case.add_deck;

import entity.Card;
import entity.User;

import java.util.ArrayList;

public class AddDeckInputData {
  private final int deckId;
  private String deckName;
  private String owner;
  private ArrayList<User> editors;
  private ArrayList<Card> cards;

  public AddDeckInputData (int deckId, String deckName, String owner){
    this.deckId = deckId;
    this.deckName = deckName;
    this.owner = owner;
    this.editors = new ArrayList<>();
    this.cards = new ArrayList<>();
  }

}
