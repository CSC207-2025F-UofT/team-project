package interface_adapter.clicking;

import interface_adapter.ViewManagerModel;
import use_case.clicking.ClickingOutputBoundary;
import use_case.clicking.ClickingOutputData;

public class ClickingPresenter implements ClickingOutputBoundary {

    private final ClickingViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public ClickingPresenter(ClickingViewModel vm, ViewManagerModel vmm) {
        this.viewModel = vm;
        this.viewManagerModel = vmm;
    }

    @Override
    public void prepareSuccessView(ClickingOutputData outputData) {
        ClickingState state = viewModel.getState();
        state.setErrorMessage("");
        state.setTitle(outputData.getTitle());
        state.setYear(outputData.getReleaseYear());
        state.setLanguage(outputData.getLanguage());
        state.setRating(outputData.getRating());
        state.setGenres(outputData.getGenres());
        state.setOverview(outputData.getOverview());
        state.setPosterUrl(outputData.getPosterUrl());

        viewManagerModel.setState(viewModel.viewName);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        ClickingState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();

        viewManagerModel.setState(viewModel.viewName);
        viewManagerModel.firePropertyChange();
    }
}
