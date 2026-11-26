package interface_adapter.ViewWatchlist;

import java.util.ArrayList;
import java.util.List;

/**
 * What the watchlist page shows: the user and their movies (title + poster).
 */
public class WatchlistState {
    private String username = "";
    private List<Item> items = new ArrayList<>();
    private String errorMessage = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Adds a movie to the page if it is not shown yet.
     * @param item the movie to show
     */
    public void addItem(Item item) {
        for (Item existing : items) {
            if (existing.getMovieId().equals(item.getMovieId())) {
                return;
            }
        }
        items.add(item);
    }

    /**
     * One movie card on the watchlist page.
     */
    public static class Item {
        private final String movieId;
        private final String title;
        private final String posterUrl;

        public Item(String movieId, String title, String posterUrl) {
            this.movieId = movieId;
            this.title = title;
            this.posterUrl = posterUrl;
        }

        public String getMovieId() {
            return movieId;
        }

        public String getTitle() {
            return title;
        }

        public String getPosterUrl() {
            return posterUrl;
        }
    }
}
