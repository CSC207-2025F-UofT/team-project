package interface_adapter.add_clothing;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import use_case.add_clothing.AddClothingOutputBoundary;
import use_case.add_clothing.AddClothingOutputData;

/**
 * Turns the Interactor's result into what the screen shows:
 * a success message (and a cleared form), or an error.
 */
public class AddClothingPresenter implements AddClothingOutputBoundary {
    private final AddClothingViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Connects the presenter to the screen's ViewModel and the screen switcher.
     * viewModel the Add Clothing ViewModel
     * viewManagerModel switches screens
     */
    public AddClothingPresenter(AddClothingViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(AddClothingOutputData outputData) {
        final AddClothingState state = viewModel.getState();
        state.setError("");
        state.setMessage(outputData.getItemName() + " was added to your closet.");
        viewModel.firePropertyChanged("success");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final AddClothingState state = viewModel.getState();
        state.setMessage("");
        state.setError(errorMessage);
        viewModel.firePropertyChanged("error");
    }

    @Override
    public void switchToWelcomeView() {
        final AddClothingState state = viewModel.getState();
        state.setError("");
        state.setMessage("");
        viewModel.firePropertyChanged("reset");

        viewManagerModel.setState(ViewNames.WELCOME);
        viewManagerModel.firePropertyChanged();
    }
}
