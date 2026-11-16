package interface_adapters.save_session;

import use_case.save_session.SaveSessionOutputBoundary;
import use_case.save_session.SaveSessionOutputData;

/**
 * The Presenter for the Save Session Use Case.
 */
public class SaveSessionPresenter implements SaveSessionOutputBoundary {

    private final SaveSessionViewModel viewModel;

    public SaveSessionPresenter(SaveSessionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Prepares the Save Session View Model for a successful execution of the use case.
     * @param outputData the output data returned by the Save Session interactor
     */
    @Override
    public void prepareSuccessView(SaveSessionOutputData outputData) {
        SaveSessionState state = viewModel.getState();
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    /**
     * Prepares the Save Session View Model for a failed execution of the use case.
     * @param errorMessage the error message describing why the use case failed
     */
    @Override
    public void prepareFailView(String errorMessage) {
        SaveSessionState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
