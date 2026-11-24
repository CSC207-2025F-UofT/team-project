package interface_adapter.meal_planning;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;

public class CreatePlanViewModel {

    private String planId;
    private LocalDate date;
    private String errorMessage = "";

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public String getPlanId() {
        return planId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void update(String planId, LocalDate date, String errorMessage) {
        this.planId = planId;
        this.date = date;
        this.errorMessage = errorMessage;

        support.firePropertyChange("createPlan", null, this);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}