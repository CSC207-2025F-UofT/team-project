package interface_adapter.viewplayerstats;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public final class ViewPlayerStatsViewModel {

    public static final String VIEW_NAME = "player_stats";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    private ViewPlayerStatsState stats =
            new ViewPlayerStatsState();

    public ViewPlayerStatsState getStats() {
        return stats;
    }
    public void setStats(ViewPlayerStatsState newState){
        ViewPlayerStatsState oldState = stats;
        stats = Objects.requireNonNull(newState);

        support.firePropertyChange("state", oldState, stats);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener){
        support.removePropertyChangeListener(listener);
    }
}
