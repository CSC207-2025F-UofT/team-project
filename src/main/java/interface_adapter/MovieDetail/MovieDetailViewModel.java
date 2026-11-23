package interface_adapter.MovieDetail;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel for the Movie Detail View.
 */
public class MovieDetailViewModel {
    public static final String VIEW_NAME = "movie detail";

    private MovieDetailState state = new MovieDetailState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public MovieDetailState getState() {
        return state;
    }

    public void setState(MovieDetailState state) {
        this.state = state;
    }

    public void notifyStateChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}