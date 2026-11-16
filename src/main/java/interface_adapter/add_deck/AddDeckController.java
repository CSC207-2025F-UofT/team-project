package interface_adapter.add_deck;

import use_case.add_card.AddCardInputData;
import use_case.add_deck.AddDeckInputBoundary;
import use_case.add_deck.AddDeckInputData;

public class AddDeckController {
  private final AddDeckInputBoundary interactor;

  public AddDeckController(AddDeckInputBoundary interactor) { this.interactor = interactor; }

  public void execute(int deckId, String name, String owner) {
    AddDeckInputData inputData = new AddDeckInputData(deckId, name, owner);

    interactor.execute(inputData);
  }
}


