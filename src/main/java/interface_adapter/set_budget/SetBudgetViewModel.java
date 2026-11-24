package interface_adapter.set_budget;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Set Budget View.
 */
public class SetBudgetViewModel {

    private final List<SetBudgetViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "set budget";

    private SetBudgetState state = new SetBudgetState();

    public SetBudgetState getState() {
        return this.state;
    }

    public void setState(SetBudgetState state) {
        this.state = state;
    }

    public void addListener(SetBudgetViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (SetBudgetViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}
