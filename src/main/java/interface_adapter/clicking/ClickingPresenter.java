package interface_adapter.clicking;

import interface_adapter.ViewManagerModel;
import interface_adapter.rate_and_comment.CommentViewModel;
import use_case.clicking.ClickingOutputBoundary;
import use_case.clicking.ClickingOutputData;

public class ClickingPresenter implements ClickingOutputBoundary {

    private final ClickingViewModel clickingViewModel;
    private final ViewManagerModel viewManagerModel;
    private final CommentViewModel commentViewModel;

    public ClickingPresenter(ClickingViewModel vm, ViewManagerModel vmm, CommentViewModel commentViewModel) {
        this.clickingViewModel = vm;
        this.viewManagerModel = vmm;
        this.commentViewModel = commentViewModel;
    }

    @Override
    public void prepareSuccessView(ClickingOutputData outputData) {
        // 清空错误信息
        clickingViewModel.errorMessage = "";

        clickingViewModel.title = outputData.title;
        clickingViewModel.releaseYear = outputData.releaseYear;
        clickingViewModel.language = outputData.language;
        clickingViewModel.rating = outputData.rating;
        clickingViewModel.genres = outputData.genres;
        clickingViewModel.overview = outputData.overview;
        clickingViewModel.posterUrl = outputData.posterUrl;

        final Comment;

        viewManagerModel.setState(clickingViewModel.viewName);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String errorMessage) {

        clickingViewModel.errorMessage = errorMessage;


        viewManagerModel.setState(clickingViewModel.viewName);
        viewManagerModel.firePropertyChange();
    }
}
