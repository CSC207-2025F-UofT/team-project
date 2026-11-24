package entity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A read-only snapshot of a recipe.
 *
 * <p>The identifier is supplied by the data source and remains the same across
 * snapshots of the same recipe. Category, area, instruction and thumbnail may
 * be {@code null} when the corresponding information is unavailable.</p>
 */
public final class Recipe {
    private final String id;
    private final String name;
    private final String category;
    private final String area;
    private final String instruction;
    private final URI thumbnail;
    private final List<RecipeIngredient> ingredients;

    /**
     * Creates a recipe snapshot.
     * Notice that this shall only be invoked by the DataAccessObject.
     *
     * @param id stable, source-qualified recipe identifier
     * @param name recipe name
     * @param category recipe category, or null if unavailable
     * @param area recipe area, or null if unavailable
     * @param instruction preparation instructions, or null if unavailable
     * @param thumbnail image location, or null if unavailable
     * @param ingredients recipe ingredients; an empty list is permitted
     * @throws NullPointerException if id, name, ingredients, or an ingredient is null
     */
    public Recipe(String id, String name, String category, String area, String instruction,
                  URI thumbnail, List<RecipeIngredient> ingredients) {
        this.id = Objects.requireNonNull(id, "recipe id cannot be null");
        this.name = Objects.requireNonNull(name, "recipe name cannot be null");
        this.category = category;
        this.area = area;
        this.instruction = instruction;
        this.thumbnail = thumbnail;
        this.ingredients = List.copyOf(ingredients);
    }

    /**
     * Returns the stable, source-qualified recipe identifier.
     *
     * @return the recipe identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the recipe name.
     *
     * @return the recipe name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the recipe category.
     *
     * @return the category, or null if unavailable
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the recipe area.
     *
     * @return the area, or null if unavailable
     */
    public String getArea() {
        return area;
    }

    /**
     * Returns the preparation instructions.
     *
     * @return the preparation instructions, or null if unavailable
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * Returns the thumbnail location.
     *
     * @return the image location, or null if unavailable
     */
    public URI getThumbnail() {
        return thumbnail;
    }

    /**
     * Returns a copy of the recipe's ingredient list.
     *
     * @return a mutable, shallow copy of the ingredient list
     */
    public List<RecipeIngredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }
}
