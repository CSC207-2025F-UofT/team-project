package user_case.Search;

import entity.User;
import entity.Movie;

/**
 * The DAO interface for the Search Use Case.
 */
public interface SearchMovieDataAccessInterface {

    /**
     * Asks for the corresponding movie method.
     * @param movie the movie
     */
    Movie getMovie(String movie);
}