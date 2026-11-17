package entity;

public class RecipeIngredient {
    private IngredientName name;
    private String measure;

    /*
    TODO
     */
    private RecipeIngredient(IngredientName name, String measure) {
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
}
