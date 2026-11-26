package interface_adapter.Favorite;

import user_case.Favorite.FavoriteInputBoundary;
import user_case.Favorite.FavoriteInputData;

/** Receives a request from the view and passes it to the favourite use case. */
public class FavoriteController {
    private final FavoriteInputBoundary favoriteUseCase;

    public FavoriteController(FavoriteInputBoundary favoriteUseCase) {
        if (favoriteUseCase == null) {
            throw new IllegalArgumentException("Favorite use case is required");
        }
        this.favoriteUseCase = favoriteUseCase;
    }

    /** Call this when the user clicks the Add to Favourites button. */
    public void addFavorite(String username, int tmdbMovieId) {
        favoriteUseCase.addFavorite(new FavoriteInputData(username, tmdbMovieId));
    }
}
