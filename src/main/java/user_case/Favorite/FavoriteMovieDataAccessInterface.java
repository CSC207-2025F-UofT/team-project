package user_case.Favorite;

import java.util.List;

/**
 * Data access boundary for a user's favourite movies.
 *
 * <p>The use case depends on this interface instead of depending on a JSON file
 * or another storage implementation.
 */
public interface FavoriteMovieDataAccessInterface {

    /**
     * Add a movie to a user's favourites.
     *
     * @param username the Flicksy username
     * @param tmdbMovieId the movie's id in TMDB
     * @return true if the favourite was newly added; false if it was already present
     */
    boolean addFavorite(String username, int tmdbMovieId);

    /**
     * Check whether a movie is already in a user's favourites.
     *
     * @param username the Flicksy username
     * @param tmdbMovieId the movie's id in TMDB
     * @return true if the user has already favourited this movie
     */
    boolean isFavorite(String username, int tmdbMovieId);

    /**
     * Return the TMDB movie ids in a user's favourites.
     *
     * @param username the Flicksy username
     * @return the user's favourite TMDB movie ids
     */
    List<Integer> getFavoriteMovieIds(String username);
}
