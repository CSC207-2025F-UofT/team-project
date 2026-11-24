package interface_adapter.Loggedin;

import use_case.get_data.get_dataOutputBoundary;
import use_case.get_data.get_dataOutputData;

/**
 * The Presenter for the get_data Use Case.
 */
public class get_dataPresenter implements get_dataOutputBoundary {

    private final LoggedinViewModel loggedinViewModel;

    public get_dataPresenter(LoggedinViewModel loggedinViewModel) {
        this.loggedinViewModel = loggedinViewModel;
    }

    @Override
    public void prepareSuccessView(get_dataOutputData outputData) {
        final LoggedinState state = loggedinViewModel.getState();
        state.setEntries(outputData.getEntries());
        state.setDataLoaded(true);
        state.setDataError("");
        loggedinViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LoggedinState state = loggedinViewModel.getState();
        state.setDataError(errorMessage == null ? "Failed to load data." : errorMessage);
        loggedinViewModel.notifyStateChanged();
    }

    @Override
    public void switchToUserViewGraphView() {
        // TODO: no graph view exists yet. Once one does, switch to it with a
        // ViewManagerModel here, as SignupPresenter does for the login view.
    }
}
