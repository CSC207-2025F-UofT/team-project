package interface_adapters.home;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel {
    private HomeState state = new HomeState();
    private List<HomeViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "home";

    public HomeState getState() {
        return state;
    }

    public void setState(HomeState state) {
        this.state = state;
    }

    public void addListener(HomeViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (HomeViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}
