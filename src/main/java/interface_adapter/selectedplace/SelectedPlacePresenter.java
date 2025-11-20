package interface_adapter.selectedplace;

import interface_adapter.ViewManagerModel;
import use_case.selectedplace.SelectedPlaceOutputBoundary;
import use_case.selectedplace.SelectedPlaceOutputData;

public class SelectedPlacePresenter implements SelectedPlaceOutputBoundary {

    private final SelectedPlaceViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public SelectedPlacePresenter(SelectedPlaceViewModel viewModel,
                                  ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void presentPlace(SelectedPlaceOutputData data) {
        SelectedPlaceState state = viewModel.getState();
        state.setUsername(data.getUsername());
        state.setLandmarkName(data.getLandmarkName());
        state.setDescription(data.getDescription());
        state.setAddress(data.getAddress());
        state.setOpenHours(data.getOpenHours());

        viewModel.setState(state);
        viewModel.firePropertyChange();

        // switch view
        viewManagerModel.setState(viewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
