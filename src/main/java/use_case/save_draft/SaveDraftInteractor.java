package use_case.save_draft;

import entity.Ingredient;
import entity.Recipe;
import use_case.post_recipe.PostRecipeInputData;
import use_case.post_recipe.PostRecipeDataAccessInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SaveDraftInteractor implements SaveDraftInputBoundary {

    private final PostRecipeDataAccessInterface recipeDataAccess;
    private final SaveDraftOutputBoundary presenter;

    public SaveDraftInteractor(PostRecipeDataAccessInterface recipeDataAccess,
                               SaveDraftOutputBoundary presenter) {
        this.recipeDataAccess = recipeDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveDraftInputData inputData) {
        String recipeId = UUID.randomUUID().toString();
        Date now = new Date();

        List<Ingredient> ingredients = new ArrayList<>();
        if (inputData.getIngredients() != null) {
            for (SaveDraftInputData.IngredientDTO dto : inputData.getIngredients()) {
                ingredients.add(new Ingredient(dto.getName(),
                        dto.getQuantity(),
                        dto.getUnit()));
            }
        }

        Recipe draft = new Recipe(
                recipeId,
                inputData.getAuthorId(),
                inputData.getTitle(),
                inputData.getDescription(),
                ingredients,
                inputData.getCategory(),
                inputData.getTags(),
                Recipe.Status.DRAFT,
                now,
                now,
                inputData.getImagePath()
        );

        Recipe saved = recipeDataAccess.saveRecipe(draft);
        SaveDraftOutputData outputData =
                new SaveDraftOutputData(saved.getRecipeId(), "Draft saved");
        presenter.prepareSuccessView(outputData);
    }
}
