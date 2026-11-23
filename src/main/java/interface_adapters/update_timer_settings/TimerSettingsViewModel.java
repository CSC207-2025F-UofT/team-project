package interface_adapters.update_timer_settings;

import java.util.ArrayList;
import java.util.List;


/**
 * The View Model for the Timer Settings View.
 */
public class TimerSettingsViewModel {
    private final List<TimerSettingsViewModelListener> listeners = new ArrayList<>();
    private TimerSettingsState state = new TimerSettingsState();

    public TimerSettingsState getState() {
        return this.state;
    }

    public void setState(TimerSettingsState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be notified when the Update Timer Settings View Model changes.
     * @param listener a View
     */
    public void addListener(TimerSettingsViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the view state has changed.
     */
    public void notifyStateChanged() {
        for (TimerSettingsViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}
