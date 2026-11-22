package user_case.MovieDetail;

import entity.Movie;

public interface MovieDetailDataAccessInterface {

    Movie getMovieById(int movieID);
}