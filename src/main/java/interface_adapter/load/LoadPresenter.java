package interface_adapter.load;

import interface_adapter.save.SaveState;
import interface_adapter.save.SaveViewModel;
import use_case.load.LoadOutputBoundary;
import use_case.load.LoadOutputData;

public class LoadPresenter implements LoadOutputBoundary {
    private final SaveViewModel viewModel;

    public LoadPresenter(SaveViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadOutputData outputData) {
        SaveState state = viewModel.getState();
        state.setMessage("Loaded " + outputData.getUsername()
                + "'s game: floor " + outputData.getFloor());
        state.setError(false);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SaveState state = viewModel.getState();
        state.setMessage(errorMessage);
        state.setError(true);
        viewModel.notifyStateChanged();
    }
}