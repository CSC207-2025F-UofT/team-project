package user_case.Favorite;

/** Implements the application rules for adding a movie to favourites. */
public class FavoriteInteractor implements FavoriteInputBoundary {
    private final FavoriteMovieDataAccessInterface favoriteDataAccessObject;
    private final FavoriteOutputBoundary presenter;

    public FavoriteInteractor(FavoriteMovieDataAccessInterface favoriteDataAccessObject,
                              FavoriteOutputBoundary presenter) {
        if (favoriteDataAccessObject == null || presenter == null) {
            throw new IllegalArgumentException("DAO and presenter are required");
        }
        this.favoriteDataAccessObject = favoriteDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void addFavorite(FavoriteInputData inputData) {
        if (inputData == null) {
            presenter.prepareFailView("No movie was selected.");
            return;
        }

        String username = inputData.getUsername();
        int tmdbMovieId = inputData.getTmdbMovieId();
        if (username == null || username.trim().isEmpty()) {
            presenter.prepareFailView("Please sign in before adding a favourite.");
            return;
        }
        if (tmdbMovieId <= 0) {
            presenter.prepareFailView("The selected movie has an invalid TMDB id.");
            return;
        }

        try {
            boolean added = favoriteDataAccessObject.addFavorite(username, tmdbMovieId);
            if (!added) {
                presenter.prepareFailView("This movie is already in your favourites.");
                return;
            }
        } catch (RuntimeException exception) {
            presenter.prepareFailView("Could not save this favourite. Please try again.");
            return;
        }

        presenter.prepareSuccessView(
                new FavoriteOutputData(username, tmdbMovieId, "Added to your favourites."));
    }
}
