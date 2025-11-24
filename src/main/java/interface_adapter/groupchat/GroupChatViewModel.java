package interface_adapter.groupchat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for Group Chat operations.
 */
public class GroupChatViewModel {

    private final String viewName = "group chat";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private GroupChatState state = new GroupChatState();

    public GroupChatViewModel() {
    }

    public String getViewName() {
        return viewName;
    }

    public GroupChatState getState() {
        return state;
    }

    public void setState(GroupChatState state) {
        this.state = state;
    }

    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}