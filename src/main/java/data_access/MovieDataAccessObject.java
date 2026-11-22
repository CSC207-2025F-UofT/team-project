package data_access;

import entity.Movie;
import user_case.Search.SearchMovieDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class MovieDataAccessObject implements SearchMovieDataAccessInterface {
    public static void main(String[] args) {
        GetMovieData data = new GetMovieData(System.getenv("token"));

        data.get("search/movie?query=Home Alone&include_adult=false&language=en-US&page=1", "m");

    }
    @Override
    public List<Movie> getMovie(String movieTitle){
        GetMovieData data = new GetMovieData(System.getenv("token"));
        data.get("search/movie?query=" + movieTitle + "&include_adult=false&language=en-US&page=1", "m");

        List<Movie> m = new ArrayList<>();
        return m;
    }
}
