package interface_adapter;

import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public LoginViewModel getLoginViewModel() {
        return null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    private final String viewName;

    private LoginState state;

    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return this.viewName;
    }

    public LoginState getState() {
        return this.state;
    }

    public void setState(LoginState state) {
        this.state = state;
    }

    public void firePropertyChange() {
        this.support.firePropertyChange("state", null, this.state);
    }

    public void firePropertyChange(String propertyName) {
        this.support.firePropertyChange(propertyName, null, this.state);
    }

}
