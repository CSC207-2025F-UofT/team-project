package use_case.add_deck;

import entity.Deck;

public class AddDeckInteractor implements AddDeckInputBoundary {
  private final AddDeckDataAccessInterface dataAccess;
  private final AddDeckOutputBoundary presenter;

  public AddDeckInteractor(AddDeckDataAccessInterface dataAccess, AddDeckOutputBoundary presenter) {
    this.dataAccess = dataAccess;
    this.presenter = presenter;
  }

  @Override
  public void execute(AddDeckInputData inputData) {
    // create the new deck here
  }

  // save our new deck if valid
  // dataAccess.saveDeck(inputData.

  // notify presenter new deck was updated]=
  // presenter.prepareSuccessView(outputData);
}
