package interface_adapter.add_card;

import use_case.add_card.AddCardOutputBoundary;
import use_case.add_card.AddCardOutputData;

public class AddCardPresenter implements AddCardOutputBoundary {
    private final AddCardViewModel viewModel;

    public AddCardPresenter(AddCardViewModel viewModel) {
        this.viewModel = viewModel;
    }

@Override
    public void prepareSuccessView(AddCardOutputData outputData) {
        viewModel.setSuccess(
            outputData.getCardId(), 
            outputData.getDeckId(), 
            outputData.getQuestion(), 
            outputData.getAnswer(),
            "Card added success"
        );
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setError(error);
    }
}