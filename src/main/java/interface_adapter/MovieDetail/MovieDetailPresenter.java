package interface_adapter.MovieDetail;

import interface_adapter.ViewManagerModel;
import user_case.MovieDetail.MovieDetailOutputBoundary;
import user_case.MovieDetail.MovieDetailOutputData;

/**
 * The Presenter for the Movie Detail Use Case.
 */
public class MovieDetailPresenter implements MovieDetailOutputBoundary {

    private final MovieDetailViewModel movieDetailViewModel;
    private final ViewManagerModel viewManagerModel;

    public MovieDetailPresenter(ViewManagerModel viewManagerModel,
                                MovieDetailViewModel movieDetailViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.movieDetailViewModel = movieDetailViewModel;
    }

    @Override
    public void prepareSuccessView(MovieDetailOutputData outputData) {
        final MovieDetailState state = movieDetailViewModel.getState();
        state.setMovieID(outputData.getMovieID());
        state.setName(outputData.getName());
        state.setDescription(outputData.getDescription());
        state.setRating(outputData.getRating());
        state.setReleaseDate(outputData.getReleaseDate());
        state.setPosterPath(outputData.getPosterPath());
        state.setErrorMessage(null);

        this.movieDetailViewModel.setState(state);
        this.movieDetailViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final MovieDetailState state = movieDetailViewModel.getState();
        state.setErrorMessage(errorMessage);
        this.movieDetailViewModel.notifyStateChanged();
    }
}