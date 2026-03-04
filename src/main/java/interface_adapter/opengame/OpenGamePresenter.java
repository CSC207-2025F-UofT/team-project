package interface_adapter.opengame;

import interface_adapter.ViewManagerModel;
import use_case.openGame.OpenGameOutputBoundary;
import use_case.openGame.OpenGameOutputData;

public class OpenGamePresenter implements OpenGameOutputBoundary {

    private final OpenGameViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public OpenGamePresenter(OpenGameViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(OpenGameOutputData outputData) {

        // Update ViewModel
        viewModel.setMessage(outputData.getMessage());
        viewModel.setState(outputData.getGameState());
        viewModel.firePropertyChange();   // Notify OpenGameView

        // Switch to Move screen
        viewManagerModel.setState("move");   // must match MoveView.getViewName()
        viewManagerModel.firePropertyChange();
    }


    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setMessage(errorMessage);
        viewModel.setState(null);
        viewModel.firePropertyChange();   // update UI (stays on same screen)
    }

    public OpenGameViewModel getViewModel() {
        return viewModel;
    }
}
