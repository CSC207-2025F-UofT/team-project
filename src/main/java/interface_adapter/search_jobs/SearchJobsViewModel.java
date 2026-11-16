package interface_adapter.search_jobs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/** Observable display state; the application publishes updates on its UI executor. */
public final class SearchJobsViewModel {
  private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
  private SearchJobsState state =
      new SearchJobsState(List.of(), "Enter keywords to find internships.", "", false, false);

  public SearchJobsState getState() {
    return state;
  }

  /** Replaces the state and notifies listeners; contains no reference to a concrete View. */
  public void setState(SearchJobsState value) {
    SearchJobsState previous = state;
    state = value;
    changes.firePropertyChange("state", previous, value);
  }

  /** Subscribes an observer to display-state replacements. */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    changes.addPropertyChangeListener(listener);
  }

  /** Unsubscribes an observer that no longer needs display updates. */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    changes.removePropertyChangeListener(listener);
  }
}
