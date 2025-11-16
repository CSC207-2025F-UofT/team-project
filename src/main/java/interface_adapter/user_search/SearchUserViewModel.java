package interface_adapter.user_search;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SearchUserViewModel extends ViewModel<SearchUserState> {
    public static final String TITLE_LABEL = "User Search";

    // Re-declare state field and PropertyChangeSupport to manage listeners
    private SearchUserState state = new SearchUserState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public SearchUserViewModel() {
        super("user search");
    }

    // FIX: Must include @Override and ensure return type matches ViewModel<T>'s abstract T getState()
    @Override
    public SearchUserState getState() {
        return state;
    }

    public void setState(SearchUserState state) {
        this.state = state;
    }


    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}