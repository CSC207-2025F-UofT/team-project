package interface_adapter.meal_planning;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class AddPlannedMealViewModel {

    public static class OptionItem {
        private final String id;
        private final String name;

        public OptionItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name; // JComboBox displays this text.
        }
    }

    private String message = "";
    private boolean success;
    private List<OptionItem> mealSlots = List.of();
    private List<OptionItem> foods = List.of();

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<OptionItem> getMealSlots() {
        return mealSlots;
    }

    public List<OptionItem> getFoods() {
        return foods;
    }

    public void update(String message, boolean success) {
        this.message = message;
        this.success = success;
        support.firePropertyChange("addPlannedMeal", null, this);
    }

    public void updateOptions(
            List<OptionItem> mealSlots,
            List<OptionItem> foods) {
        this.mealSlots = List.copyOf(mealSlots);
        this.foods = List.copyOf(foods);
        this.message = "";
        this.success = false;
        support.firePropertyChange(
                "addPlannedMealOptions", null, this);
    }

    public void clearOptions() {
        mealSlots = List.of();
        foods = List.of();
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}