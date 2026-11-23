package interface_adapter.edit_card;

import use_case.edit_card.EditCardOutputBoundary;
import use_case.edit_card.EditCardOutputData;

public class EditCardPresenter implements EditCardOutputBoundary {
    private EditCardViewModel viewModel;

    public EditCardPresenter(EditCardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(EditCardOutputData outputData) {
        viewModel.setSuccess(
                outputData.getDeckId(),
                outputData.getCardId(),
                outputData.getQuestion(),
                outputData.getAnswer(),
                "Card update success!"
        );
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setError(error);
    }
}
