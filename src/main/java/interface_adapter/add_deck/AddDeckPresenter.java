package interface_adapter.add_deck;

import interface_adapter.add_card.AddCardViewModel;
import use_case.add_deck.AddDeckOutputBoundary;
import use_case.add_deck.AddDeckOutputData;

public class AddDeckPresenter implements AddDeckOutputBoundary{
  private final AddDeckViewModel viewModel;

  public AddDeckPresenter(AddDeckViewModel viewModel){ this.viewModel = viewModel; }

  @Override
    public void prepareSuccessView(AddDeckOutputData outputData) {
      viewModel.setSuccess(
        outputData.getId(),
        outputData.getName(),
        outputData.getOwner(),
        "Deck added successfully"
        );
}

  @Override
    public void prepareFailureView(String error) { viewModel.setError(error);}
}
