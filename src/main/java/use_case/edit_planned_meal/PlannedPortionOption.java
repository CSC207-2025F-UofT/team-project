package use_case.edit_planned_meal;


public class PlannedPortionOption {

    private final int index;
    private final String foodName;
    private final double grams;
    private final double calories;

    public PlannedPortionOption(
            int index, String foodName,
            double grams, double calories) {
        this.index = index;
        this.foodName = foodName;
        this.grams = grams;
        this.calories = calories;
    }

    public int getIndex() {
        return index;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getGrams() {
        return grams;
    }

    public double getCalories() {
        return calories;
    }
}
