package use_case.meal_plan;

/**
 * One ingredient of a planned meal, as plain data for the presenters.
 */
public class PlannedIngredientOutputData {

    private final String name;
    private final String measure;

    /**
     * Creates output data for one ingredient.
     *
     * @param name the ingredient name to show
     * @param measure the amount, or null or blank if the recipe gives none
     */
    public PlannedIngredientOutputData(String name, String measure) {
        this.name = name;
        this.measure = measure == null ? "" : measure.trim();
    }

    /**
     * Returns the ingredient name to show.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the amount, or an empty string if the recipe gives none.
     *
     * @return the measure
     */
    public String getMeasure() {
        return measure;
    }
}
