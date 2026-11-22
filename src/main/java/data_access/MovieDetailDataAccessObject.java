package data_access;

import entity.Movie;
import user_case.MovieDetail.MovieDetailDataAccessInterface;

import java.io.IOException;

public class MovieDetailDataAccessObject implements MovieDetailDataAccessInterface {

    private final GetMovieData getMovieData;

    public MovieDetailDataAccessObject(GetMovieData getMovieData) {
        this.getMovieData = getMovieData;
    }

    @Override
    public Movie getMovieById(int movieID) {
        try {
            String rawJson = getMovieData.get("movie/" + movieID);
            if (rawJson == null || rawJson.isBlank()) {
                return null;
            }
            return new Movie(
                    "Movie #" + movieID,
                    0,
                    rawJson,
                    ""
            );
        } catch (IOException e) {
            return null;
        }
    }
}