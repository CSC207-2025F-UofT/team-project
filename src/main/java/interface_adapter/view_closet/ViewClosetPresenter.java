package interface_adapter.view_closet;
// receives filtered items from interactor, storing them in state, and signals that the view can update


import use_case.view_closet.ViewClosetOutputBoundary;
import use_case.view_closet.ViewClosetOutputData;

public class ViewClosetPresenter implements ViewClosetOutputBoundary {
    private final ViewClosetViewModel viewModel;

    public ViewClosetPresenter(ViewClosetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ViewClosetOutputData outputData) {
        ViewClosetState state = viewModel.getState();
        state.setItems(outputData.getItems());
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}






