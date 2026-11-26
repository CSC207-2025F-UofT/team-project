package user_case.Search;

import entity.User;
import entity.Movie;

import java.util.List;

/**
 * The DAO interface for the Search Use Case.
 */
public interface SearchMovieDataAccessInterface {

    /**
     * Asks for the corresponding movie method.
     * @param movieTitle the movie
     */
    List<Movie> getMovie(String movieTitle);
}