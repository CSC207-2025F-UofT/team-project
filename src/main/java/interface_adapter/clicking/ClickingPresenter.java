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
        // 清空错误信息
        viewModel.errorMessage = "";

        viewModel.title = outputData.title;
        viewModel.releaseYear = outputData.releaseYear;
        viewModel.language = outputData.language;
        viewModel.rating = outputData.rating;
        viewModel.genres = outputData.genres;
        viewModel.overview = outputData.overview;
        viewModel.posterUrl = outputData.posterUrl;

        viewManagerModel.setState(viewModel.viewName);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String errorMessage) {

        viewModel.errorMessage = errorMessage;


        viewManagerModel.setState(viewModel.viewName);
        viewManagerModel.firePropertyChange();
    }
}
