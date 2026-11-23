package interface_adapter.rest;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Holds the text shown by the residence view and notifies it of changes.
 */
public class RestViewModel {
    public static final String VIEW_NAME = "residence";
    public static final String STATE_PROPERTY = "state";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String dayText = "";
    private String weatherText = "";
    private String energyText = "";
    private String saveText = "";
    private boolean retryAvailable;

    public void setState(String dayText, String weatherText, String energyText) {
        this.dayText = dayText;
        this.weatherText = weatherText;
        this.energyText = energyText;
        this.saveText = "";
        this.retryAvailable = false;
        support.firePropertyChange(STATE_PROPERTY, null, this);
    }

    public void setSaveStatus(String saveText, boolean retryAvailable) {
        this.saveText = saveText;
        this.retryAvailable = retryAvailable;
        support.firePropertyChange(STATE_PROPERTY, null, this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public String getDayText() {
        return dayText;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public String getEnergyText() {
        return energyText;
    }

    public String getSaveText() {
        return saveText;
    }

    public boolean isRetryAvailable() {
        return retryAvailable;
    }
}
