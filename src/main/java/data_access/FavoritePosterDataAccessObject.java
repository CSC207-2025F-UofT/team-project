package data_access;

import entity.Movie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/** Looks up one saved movie's title and individual poster for the favorites gallery. */
public final class FavoritePosterDataAccessObject {
    private final GetMovieData client;

    public FavoritePosterDataAccessObject(String token) {
        client = token == null || token.isBlank() ? null : new GetMovieData(token);
    }

    /** Returns null when TMDB is unavailable, allowing the caller to use its search cache. */
    public Movie getMovieById(int id) {
        if (client == null || id <= 0) {
            return null;
        }
        try {
            JSONObject movie = new JSONObject(client.get("movie/" + id));
            if (movie.optInt("id", -1) != id) {
                return null;
            }
            String title = movie.optString("title", "");
            if (title.isBlank()) {
                title = movie.optString("original_title", "");
            }
            // Nested belongs_to_collection.poster_path describes the whole collection.
            String poster = movie.optString("poster_path", "");
            return new Movie(title.isBlank() ? "Movie #" + id : title, "", "", id, poster);
        } catch (IOException | JSONException error) {
            return null;
        }
    }
}
