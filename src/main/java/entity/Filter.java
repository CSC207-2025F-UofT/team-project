package entity;

import java.util.List;

/**
 * Entity representing a recipe filter.
 */
public class Filter {

    private final Integer cookingTimeMax;
    private final String cuisine;
    private final String mealType;
    private final List<String> excludeIngredients;
    private final List<Tag> includedTags;

    /**
     * Creates a new Filter entity.
     *
     * @param cookingTimeMax maximum cooking time
     * @param cuisine type of cuisine
     * @param mealType type of meal
     * @param excludeIngredients list of ingredients to exclude
     * @param includedTags list of tags to include
     * @throws IllegalArgumentException if the password or name are empty
     */
    public Filter(Integer cookingTimeMax,
                  String cuisine,
                  String mealType,
                  List<String> excludeIngredients,
                  List<Tag> includedTags) {
        if (excludeIngredients == null) {
            throw new IllegalArgumentException("Exclude ingredients list cannot be null");
        }
        if (includedTags == null) {
            throw new IllegalArgumentException("Included tags list cannot be null");
        }

        this.cookingTimeMax = cookingTimeMax;
        this.cuisine = cuisine;
        this.mealType = mealType;
        this.excludeIngredients = excludeIngredients;
        this.includedTags = includedTags;
    }

    public Integer getCookingTimeMax() { return cookingTimeMax; }
    public String getCuisine() { return cuisine; }
    public String getMealType() { return mealType; }
    public List<String> getExcludeIngredients() { return excludeIngredients; }
    public List<Tag> getIncludedTags() { return includedTags; }

    @Override
    public String toString() {
        return "Filter{" +
                "time<=" + cookingTimeMax +
                ", cuisine='" + cuisine + '\'' +
                ", mealType='" + mealType + '\'' +
                ", exclude=" + excludeIngredients +
                ", tags=" + includedTags +
                '}';
    }
}
