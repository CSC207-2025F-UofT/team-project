package data_access;

import entity.WatchlistMovie;
import org.json.JSONArray;
import org.json.JSONObject;
import user_case.AddToWatchlist.AddToWatchlistDataAccessInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores each user's watchlist locally as a JSON file, keyed by username.
 * Backs the Add to Watchlist use case.
 * TMDB has no concept of this app's own user accounts, so the watchlist is
 * not fetched from the TMDB API -- it is data this app owns and persists itself.
 */
public class WatchlistDataAccessObject implements AddToWatchlistDataAccessInterface {

    private final File file;
    private final Map<String, List<WatchlistMovie>> watchlist = new LinkedHashMap<>();

    public WatchlistDataAccessObject(String filePath) {
        this.file = new File(filePath);
        load();
    }

    @Override
    public boolean isInWatchlist(String username, String movieId) {
        for (WatchlistMovie movie : watchlist.getOrDefault(username, List.of())) {
            if (movie.getId().equals(movieId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addToWatchlist(String username, WatchlistMovie movie) {
        watchlist.computeIfAbsent(username, key -> new ArrayList<>()).add(movie);
        save();
    }

    private void load() {
        if (!file.exists()) {
            return;
        }
        try {
            final String content = Files.readString(file.toPath());
            if (content.isBlank()) {
                return;
            }
            final JSONObject root = new JSONObject(content);
            for (String username : root.keySet()) {
                final JSONArray moviesJson = root.getJSONArray(username);
                final List<WatchlistMovie> movies = new ArrayList<>();
                for (int i = 0; i < moviesJson.length(); i++) {
                    final JSONObject movieJson = moviesJson.getJSONObject(i);
                    movies.add(new WatchlistMovie(
                            movieJson.getString("id"),
                            movieJson.getString("title"),
                            movieJson.optString("posterUrl", "")));
                }
                watchlist.put(username, movies);
            }
        }
        catch (IOException ex) {
            throw new UncheckedIOException("Could not read watchlist file: " + file, ex);
        }
    }

    private void save() {
        final JSONObject root = new JSONObject();
        for (Map.Entry<String, List<WatchlistMovie>> entry : watchlist.entrySet()) {
            final JSONArray moviesJson = new JSONArray();
            for (WatchlistMovie movie : entry.getValue()) {
                final JSONObject movieJson = new JSONObject();
                movieJson.put("id", movie.getId());
                movieJson.put("title", movie.getTitle());
                movieJson.put("posterUrl", movie.getPosterUrl());
                moviesJson.put(movieJson);
            }
            root.put(entry.getKey(), moviesJson);
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(root.toString(2));
        }
        catch (IOException ex) {
            throw new UncheckedIOException("Could not save watchlist file: " + file, ex);
        }
    }
}
