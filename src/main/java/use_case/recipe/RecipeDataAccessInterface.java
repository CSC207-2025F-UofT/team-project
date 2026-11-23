package use_case.recipe;
import entity.Recipe;
import java.util.*;

public interface RecipeDataAccessInterface {
    List<Recipe> searchByName(String keyword);
    List<Recipe> filterByCategory(String category);
    List<String> getCategories();
    Recipe getRecipeByID(String is);
}
