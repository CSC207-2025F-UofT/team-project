package interface_adapter.MoviePoster;

import user_case.MoviePoster.MoviePosterOutputBoundary;
import user_case.MoviePoster.MoviePosterOutputData;

/**
 * Presenter for the View Movie Poster Use Case.
 */
public class MoviePosterPresenter implements MoviePosterOutputBoundary {
    private final MoviePosterViewModel viewModel;

    public MoviePosterPresenter(MoviePosterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void preparePosterView(MoviePosterOutputData outputData) {
        MoviePosterState state = viewModel.getState();
        state.setMovieTitle(outputData.getMovieTitle());
        state.setPosterImage(outputData.getPosterImage());
        state.setPlaceholder(outputData.isPlaceholder());
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}