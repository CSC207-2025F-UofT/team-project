package interface_adapter.search_event;

import entity.Event;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class SearchEventViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<Event> events;
    private String errorMessage;

    public void setEvents(List<Event> events) {
        this.events = events;
        support.firePropertyChange("events", null, events);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        support.firePropertyChange("errorMessage", null, errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
