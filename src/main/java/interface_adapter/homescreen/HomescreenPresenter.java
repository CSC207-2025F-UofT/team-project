package interface_adapter.homescreen;

import interface_adapter.ViewManagerModel;
import interface_adapter.browselandmarks.BrowseLandmarksState;
import interface_adapter.browselandmarks.BrowseLandmarksViewModel;
import use_case.homescreen.HomescreenOutputBoundary;
import use_case.homescreen.HomescreenOutputData;

public class HomescreenPresenter implements HomescreenOutputBoundary {
    private final HomescreenViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final BrowseLandmarksViewModel browseLandmarksViewModel;

    public HomescreenPresenter(HomescreenViewModel viewModel, ViewManagerModel viewManagerModel, BrowseLandmarksViewModel browseLandmarksViewModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        this.browseLandmarksViewModel = browseLandmarksViewModel;
    }

    @Override
    public void prepareSuccessView(HomescreenOutputData outputData) {
        //navigate to the target view (when views exist)
        System.out.println("Navigated to: " + outputData.getViewToNavigateTo());
        //uncomment when views exist:
        HomeScreenState homeState = viewModel.getState();
        String username = homeState.getUsername();
        BrowseLandmarksState blState = browseLandmarksViewModel.getState();
        blState.setUsername(username);   // <── store user here
        browseLandmarksViewModel.setState(blState);
        browseLandmarksViewModel.firePropertyChange();
        viewManagerModel.setState(outputData.getViewToNavigateTo());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        HomeScreenState state = viewModel.getState();
        state.setErrorMessage(error);
        viewModel.firePropertyChange();
        System.out.println("Error: " + error);
    }
}