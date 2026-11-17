package interface_adapter.calculate_grade;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Grade Calculator pop-up.
 */
public class GradeCalculatorViewModel {

    private final List<GradeCalculatorViewModelListener> listeners = new ArrayList<>();

    private GradeCalculatorState state = new GradeCalculatorState();

    public GradeCalculatorState getState() {
        return this.state;
    }

    public void setState(GradeCalculatorState state) {
        this.state = state;
    }

    public void addListener(GradeCalculatorViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (GradeCalculatorViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
