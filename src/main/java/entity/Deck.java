package entity;

// how to ensure our card id is unique each time?

import java.util.ArrayList;

public class Deck {
  private final int deckId;
  private String deckName;
  private String owner;
  private ArrayList<User> editors;
  private ArrayList<Card> cards;


  public Deck(int deckId, String deckName, String owner) {
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

  public void add_card(Card card) {
    this.cards.add(card);
  }

  // Remove card logic
  public void remove_card(int id) {

    for (int i = 0; i < cards.size(); i++) {
      if (cards.get(i).getId() == id) {
        cards.remove(i);
      break;
      }
    } // end of for loop checking each card

    // if card not found silently return, add error message here
    return;
    }
  }


