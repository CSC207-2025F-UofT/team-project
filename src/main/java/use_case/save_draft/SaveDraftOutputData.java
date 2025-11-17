package use_case.save_draft;

public class SaveDraftOutputData {
    private final String recipeId;
    private final String message;

    public SaveDraftOutputData(String recipeId, String message) {
        this.recipeId = recipeId;
        this.message = message;
    }

    public String getRecipeId() { return recipeId; }
    public String getMessage() { return message; }
}