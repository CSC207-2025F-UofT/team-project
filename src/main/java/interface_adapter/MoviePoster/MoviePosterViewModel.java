package interface_adapter.MoviePoster;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for displaying movie posters.
 */
public class MoviePosterViewModel {
    public static final String POSTER_UPDATED_PROPERTY = "posterUpdated";
    private MoviePosterState state = new MoviePosterState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public MoviePosterState getState() {
        return state;
    }

    public void setState(MoviePosterState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange(POSTER_UPDATED_PROPERTY, null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}