package interface_adapter.homescreen;

import interface_adapter.ViewManagerModel;
import use_case.homescreen.HomescreenOutputBoundary;
import use_case.homescreen.HomescreenOutputData;

public class HomescreenPresenter implements HomescreenOutputBoundary {
    private final HomescreenViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public HomescreenPresenter(HomescreenViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(HomescreenOutputData outputData) {
        //navigate to the target view (when views exist)
        System.out.println("Navigated to: " + outputData.getViewToNavigateTo());
        //uncomment when views exist:
        viewManagerModel.setState(outputData.getViewToNavigateTo());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        HomescreenState state = viewModel.getState();
        state.setErrorMessage(error);
        viewModel.firePropertyChange();
        System.out.println("Error: " + error);
    }
}