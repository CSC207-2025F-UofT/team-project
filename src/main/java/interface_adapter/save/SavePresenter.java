package interface_adapter.save;

import use_case.save.SaveOutputBoundary;
import use_case.save.SaveOutputData;

public class SavePresenter implements SaveOutputBoundary {
    private final SaveViewModel viewModel;

    public SavePresenter(SaveViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SaveOutputData outputData) {
        SaveState state = viewModel.getState();
        state.setMessage("Game saved! " + outputData.getUsername()
                + "'s deepest floor: " + outputData.getDeepestFloor());
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
