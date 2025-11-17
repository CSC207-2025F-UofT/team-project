package use_case.save_draft;

import java.util.List;
import entity.Ingredient;

public class SaveDraftInputData {
    private final String authorId;
    private final String title;
    private final String description;
    private final List<Ingredient> ingredients;
    private final String category;
    private final String imagePath;
    private final java.util.List<String> tags;

    public SaveDraftInputData(String authorId,
                              String title,
                              String description,
                              List<Ingredient> ingredients,
                              String category,
                              java.util.List<String> tags,
                              String imagePath) {
        this.authorId = authorId;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.category = category;
        this.tags = tags;
        this.imagePath = imagePath;
    }

    public String getAuthorId() { return authorId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public String getCategory() { return category; }
    public java.util.List<String> getTags() { return tags; }
    public String getImagePath() { return imagePath; }
}