package data_access;

import entity.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import user_case.Search.SearchMovieDataAccessInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieDataAccessObject implements SearchMovieDataAccessInterface {

    @Override
    public List<Movie> getMovie(String movieTitle){
        GetMovieData data = new GetMovieData(System.getenv("token"));

        try {
            String jsonResponse = data.get("search/movie?query=" + movieTitle + "&include_adult=false&language=en-US&page=1");
            JSONObject responseJSONobject = new JSONObject(jsonResponse);
            JSONArray results = responseJSONobject.getJSONArray("results");

            List<Movie> movies = new ArrayList<>();

            for(int i =0; i < results.length(); i++){
                if(results.getJSONObject(i).isNull("poster_path")) {
                    movies.add(new Movie(
                            results.getJSONObject(i).getString("title"),
                            results.getJSONObject(i).getString("overview"),
                            results.getJSONObject(i).getString("release_date"),
                            results.getJSONObject(i).getInt("id"),
                            ""));

                } else {
                    movies.add(new Movie(
                            results.getJSONObject(i).getString("title"),
                            results.getJSONObject(i).getString("overview"),
                            results.getJSONObject(i).getString("release_date"),
                            results.getJSONObject(i).getInt("id"),
                            results.getJSONObject(i).getString("poster_path")));
                }
            }
            return movies;


        } catch (IOException e) {
            System.out.println("Failed to retrieve movie data.");
            e.printStackTrace();
            return new ArrayList<>(); // confirm if wanna keep this
        }
    }
}
