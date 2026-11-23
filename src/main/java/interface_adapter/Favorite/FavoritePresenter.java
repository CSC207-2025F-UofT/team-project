package interface_adapter.Favorite;

import user_case.Favorite.FavoriteOutputBoundary;
import user_case.Favorite.FavoriteOutputData;

/** Converts use-case results into state that the view can display. */
public class FavoritePresenter implements FavoriteOutputBoundary {
    private final FavoriteViewModel viewModel;

    public FavoritePresenter(FavoriteViewModel viewModel) {
        if (viewModel == null) {
            throw new IllegalArgumentException("Favorite view model is required");
        }
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(FavoriteOutputData outputData) {
        viewModel.setState(new FavoriteState(
                outputData.getUsername(),
                outputData.getTmdbMovieId(),
                outputData.getMessage(),
                true));
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setState(new FavoriteState("", 0, message, false));
    }
}
