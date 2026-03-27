package interface_adapter.spending_limits;

import interface_adapter.ViewModel;

import java.util.Map;

public class SpendingLimitsViewModel extends ViewModel<SpendingLimitsState> {

    public SpendingLimitsViewModel() {
        super("spending limits");
        setState(new SpendingLimitsState());
    }

    public Map<String, Double> getLimits() {
        return getState().getLimits();
    }

    public void setLimits(Map<String, Double> limits) {
        getState().setLimits(limits);
        firePropertyChange();
    }
}
