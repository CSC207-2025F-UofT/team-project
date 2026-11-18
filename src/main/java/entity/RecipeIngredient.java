package entity;

public class RecipeIngredient {
    private IngredientName name;
    private String measure;

    /*
    TODO
     */
    public RecipeIngredient(IngredientName name, String measure) {
        this.name = name;
        this.measure = measure;
    }

    /*
    TODO
     */
    public String getMeasureDescription() {
        return this.measure;
    }

    /*
    TODO
     */
    public String getDisplayName() {
        return this.name.getDisplayName();
    }

    
    /**
     * Returns the ingredient name as a value object, so ingredients can be
     * compared with IngredientName.equals instead of raw strings.
     *
     * @return the ingredient name
     */
    public IngredientName getName() {
        return this.name;
    }
}
