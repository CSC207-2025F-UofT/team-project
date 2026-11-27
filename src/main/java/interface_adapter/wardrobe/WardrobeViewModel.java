package interface_adapter.wardrobe;

import use_case.add_clothing.WardrobeOutputData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class WardrobeViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private WardrobeOutputData state;
    public WardrobeOutputData getState() { return state; }
    public void setState(WardrobeOutputData state) {
        this.state = state;
        changes.firePropertyChange("state", null, state);
    }
    public void showError(String message) { changes.firePropertyChange("error", null, message); }
    public void setBusy(boolean busy) { changes.firePropertyChange("busy", null, busy); }
    public void addListener(PropertyChangeListener listener) { changes.addPropertyChangeListener(listener); }
    public void removeListener(PropertyChangeListener listener) { changes.removePropertyChangeListener(listener); }
}
