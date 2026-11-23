package interface_adapter.outfit;

import use_case.outfit.OutfitOutputData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class OutfitViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private OutfitOutputData state;
    public OutfitOutputData getState() { return state; }
    public void setState(OutfitOutputData state) {
        this.state = state;
        changes.firePropertyChange("state", null, state);
    }
    public void showError(String message) { changes.firePropertyChange("error", null, message); }
    public void setBusy(boolean busy) { changes.firePropertyChange("busy", null, busy); }
    public void addListener(PropertyChangeListener listener) { changes.addPropertyChangeListener(listener); }
    public void removeListener(PropertyChangeListener listener) { changes.removePropertyChangeListener(listener); }
}
