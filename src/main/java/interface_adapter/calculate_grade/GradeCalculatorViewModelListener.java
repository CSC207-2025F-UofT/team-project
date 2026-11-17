package interface_adapter.calculate_grade;

/**
 * Listens for changes to the Grade Calculator View Model.
 */
public interface GradeCalculatorViewModelListener {

    /**
     * A grade calculation finished, successfully or not.
     */
    void stateChanged();
}
