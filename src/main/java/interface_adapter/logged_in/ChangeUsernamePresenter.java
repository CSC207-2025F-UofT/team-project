package interface_adapter.logged_in;

import interface_adapter.ViewManagerModel;
import use_case.change_username.ChangeUsernameOutputBoundary;
import use_case.change_username.ChangeUsernameOutputData;

public class ChangeUsernamePresenter implements ChangeUsernameOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public ChangeUsernamePresenter(LoggedInViewModel loggedInViewModel, ViewManagerModel viewManagerModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ChangeUsernameOutputData response) {
        // Update the state with the new username and clear error
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getNewUsername());
        loggedInState.setUsernameError(null);

        // Notify views (LoggedInView and AccountDetailsView) of the successful username change
        loggedInViewModel.firePropertyChange("username");
    }

    @Override
    public void prepareFailView(String error) {
        // Update the state with the error message
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsernameError(error);

        // Notify views to display the error
        loggedInViewModel.firePropertyChange("username");
    }
}