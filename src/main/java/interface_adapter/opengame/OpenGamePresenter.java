package interface_adapter.opengame;

import use_case.openGame.OpenGameOutputBoundary;
import use_case.openGame.OpenGameOutputData;

public class OpenGamePresenter implements OpenGameOutputBoundary {

    private final OpenGameViewModel viewModel;

    public OpenGamePresenter(OpenGameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(OpenGameOutputData outputData) {
        viewModel.setMessage(outputData.getMessage());
        viewModel.setState(outputData.getGameState());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setMessage(errorMessage);
        viewModel.setState(null);
    }

    public OpenGameViewModel getViewModel() {
        return viewModel;
    }
}
