package data_access;

import org.json.JSONArray;
import org.json.JSONObject;
import user_case.Favorite.FavoriteMovieDataAccessInterface;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/** Stores favourite TMDB movie IDs in a local JSON object keyed by Flicksy username. */
public final class FavoriteDataAccessObject implements FavoriteMovieDataAccessInterface {
    private static final Object WRITE_LOCK = new Object();
    private final Path file;

    public FavoriteDataAccessObject(String filePath) {
        file = Path.of(filePath).toAbsolutePath().normalize();
    }

    @Override
    public boolean addFavorite(String username, int tmdbMovieId) {
        if (username == null || username.isBlank() || tmdbMovieId <= 0) {
            throw new IllegalArgumentException("A username and a positive movie ID are required.");
        }

        synchronized (WRITE_LOCK) {
            try {
                Files.createDirectories(file.getParent());
                Path lockFile = file.resolveSibling(file.getFileName() + ".lock");
                try (FileChannel channel = FileChannel.open(lockFile,
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                     FileLock lock = channel.lock()) {
                    JSONObject favorites = load();
                    List<Integer> ids = movieIds(favorites, username);
                    if (ids.contains(tmdbMovieId)) {
                        return false;
                    }
                    ids.add(tmdbMovieId);
                    favorites.put(username, new JSONArray(ids));
                    save(favorites);
                    return true;
                }
            } catch (IOException exception) {
                throw new UncheckedIOException("Could not save favourites to " + file, exception);
            }
        }
    }

    @Override
    public boolean isFavorite(String username, int tmdbMovieId) {
        return getFavoriteMovieIds(username).contains(tmdbMovieId);
    }

    @Override
    public List<Integer> getFavoriteMovieIds(String username) {
        try {
            return movieIds(load(), username);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not read favourites from " + file, exception);
        }
    }

    private JSONObject load() throws IOException {
        try {
            String content = Files.readString(file);
            return content.isBlank() ? new JSONObject() : new JSONObject(content);
        } catch (NoSuchFileException missing) {
            return new JSONObject();
        }
    }

    private static List<Integer> movieIds(JSONObject favorites, String username) {
        List<Integer> ids = new ArrayList<>();
        if (favorites.has(username)) {
            JSONArray movies = favorites.getJSONArray(username);
            for (int i = 0; i < movies.length(); i++) {
                ids.add(movies.getInt(i));
            }
        }
        return ids;
    }

    private void save(JSONObject favorites) throws IOException {
        Path temp = Files.createTempFile(file.getParent(), "favorites-", ".tmp");
        try {
            Files.writeString(temp, favorites.toString(2));
            Files.move(temp, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temp);
        }
    }
}
