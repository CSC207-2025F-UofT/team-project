package interface_adapter.AddToWatchlist;

/**
 * What the "Add to Watchlist" button needs to show after a click.
 */
public class AddToWatchlistState {
    private String message = "";
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
