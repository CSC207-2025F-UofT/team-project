package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.move_planned_meal.MovePlannedMealInputData;
import use_case.move_planned_meal.MovePlannedMealInteractor;
import use_case.move_planned_meal.MovePlannedMealOutputBoundary;
import use_case.move_planned_meal.MovePlannedMealOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileMealPlanDataAccessObjectTest {
    /** A Monday, because MealPlan only accepts Mondays as week start. */
    private static final LocalDate WEEK_A = LocalDate.of(2026, 11, 23);
    /** The Monday after WEEK_A. */
    private static final LocalDate WEEK_B = LocalDate.of(2026, 11, 30);
    private static final MealSlot MONDAY_DINNER = new MealSlot(DayOfWeek.MONDAY, MealType.DINNER);
    private static final MealSlot TUESDAY_BREAKFAST =
            new MealSlot(DayOfWeek.TUESDAY, MealType.BREAKFAST);

    @TempDir
    private Path tempDirectory;

    @Test
    void createsMissingFileAndParentFolders() {
        final Path filePath = tempDirectory.resolve("nested/folder/mealplans.json");

        final FileMealPlanDataAccessObject dataAccess = new FileMealPlanDataAccessObject(filePath);

        assertTrue(Files.exists(filePath));
        assertNull(dataAccess.getMealPlan(WEEK_A));
    }

    @Test
    void roundTripKeepsSlotsNamesMeasuresAndCookedFlag() {
        final Path filePath = tempDirectory.resolve("mealplans.json");
        final MealPlan plan = new MealPlan(WEEK_A);
        plan.assign(MONDAY_DINNER, pastaMeal());
        final PlannedMeal pancakes = pancakesMeal();
        pancakes.markCooked();
        plan.assign(TUESDAY_BREAKFAST, pancakes);

        new FileMealPlanDataAccessObject(filePath).saveMealPlan(plan);
        final MealPlan loaded = new FileMealPlanDataAccessObject(filePath).getMealPlan(WEEK_A);

        assertEquals(WEEK_A, loaded.getWeekStart());
        assertEquals(plan.getMeals().keySet(), loaded.getMeals().keySet());

        final PlannedMeal loadedPasta = loaded.getMeals().get(MONDAY_DINNER);
        assertEquals("52771", loadedPasta.getRecipeId());
        assertEquals("Spicy Arrabiata Penne", loadedPasta.getRecipeName());
        assertFalse(loadedPasta.isCooked());
        assertSameIngredients(pastaMeal().getIngredients(), loadedPasta.getIngredients());

        final PlannedMeal loadedPancakes = loaded.getMeals().get(TUESDAY_BREAKFAST);
        assertEquals("Pancakes", loadedPancakes.getRecipeName());
        assertTrue(loadedPancakes.isCooked());
        assertSameIngredients(pancakesMeal().getIngredients(), loadedPancakes.getIngredients());
    }

    @Test
    void returnsNullForWeekWithoutPlan() {
        final FileMealPlanDataAccessObject dataAccess =
                new FileMealPlanDataAccessObject(tempDirectory.resolve("mealplans.json"));
        final MealPlan plan = new MealPlan(WEEK_A);
        plan.assign(MONDAY_DINNER, pastaMeal());
        dataAccess.saveMealPlan(plan);

        assertNull(dataAccess.getMealPlan(WEEK_B));
    }

    @Test
    void savingAnotherWeekKeepsEarlierWeek() {
        final FileMealPlanDataAccessObject dataAccess =
                new FileMealPlanDataAccessObject(tempDirectory.resolve("mealplans.json"));
        final MealPlan weekA = new MealPlan(WEEK_A);
        weekA.assign(MONDAY_DINNER, pastaMeal());
        final MealPlan weekB = new MealPlan(WEEK_B);
        weekB.assign(TUESDAY_BREAKFAST, pancakesMeal());

        dataAccess.saveMealPlan(weekA);
        dataAccess.saveMealPlan(weekB);

        assertEquals("Spicy Arrabiata Penne",
                dataAccess.getMealPlan(WEEK_A).getMeals().get(MONDAY_DINNER).getRecipeName());
        assertEquals("Pancakes",
                dataAccess.getMealPlan(WEEK_B).getMeals().get(TUESDAY_BREAKFAST).getRecipeName());
    }

    @Test
    void savingSameWeekReplacesThatWeekOnly() {
        final FileMealPlanDataAccessObject dataAccess =
                new FileMealPlanDataAccessObject(tempDirectory.resolve("mealplans.json"));
        final MealPlan weekA = new MealPlan(WEEK_A);
        weekA.assign(MONDAY_DINNER, pastaMeal());
        final MealPlan weekB = new MealPlan(WEEK_B);
        weekB.assign(MONDAY_DINNER, pastaMeal());
        dataAccess.saveMealPlan(weekA);
        dataAccess.saveMealPlan(weekB);

        final MealPlan newWeekA = new MealPlan(WEEK_A);
        newWeekA.assign(TUESDAY_BREAKFAST, pancakesMeal());
        dataAccess.saveMealPlan(newWeekA);

        assertEquals(List.of(TUESDAY_BREAKFAST),
                List.copyOf(dataAccess.getMealPlan(WEEK_A).getMeals().keySet()));
        assertEquals(List.of(MONDAY_DINNER),
                List.copyOf(dataAccess.getMealPlan(WEEK_B).getMeals().keySet()));
    }

    @Test
    void corruptFileThrowsIllegalStateException() throws IOException {
        final Path filePath = tempDirectory.resolve("mealplans.json");
        Files.writeString(filePath, "{not valid json", StandardCharsets.UTF_8);
        final FileMealPlanDataAccessObject dataAccess = new FileMealPlanDataAccessObject(filePath);

        assertThrows(IllegalStateException.class, () -> dataAccess.getMealPlan(WEEK_A));
        assertThrows(IllegalStateException.class,
                () -> dataAccess.saveMealPlan(new MealPlan(WEEK_A)));
    }

    @Test
    void badMealDataThrowsIllegalStateException() throws IOException {
        final Path filePath = tempDirectory.resolve("mealplans.json");
        Files.writeString(filePath,
                "{\"weeks\": {\"2026-11-23\": [{\"day\": \"FUNDAY\"}]}}", StandardCharsets.UTF_8);
        final FileMealPlanDataAccessObject dataAccess = new FileMealPlanDataAccessObject(filePath);

        assertThrows(IllegalStateException.class, () -> dataAccess.getMealPlan(WEEK_A));
    }

    @Test
    void blankFileLoadsAsNoPlans() throws IOException {
        final Path filePath = tempDirectory.resolve("mealplans.json");
        Files.writeString(filePath, "   ", StandardCharsets.UTF_8);

        final FileMealPlanDataAccessObject dataAccess = new FileMealPlanDataAccessObject(filePath);

        assertNull(dataAccess.getMealPlan(WEEK_A));
    }

    @Test
    void moveUseCasePersistsThroughFileDao() {
        final Path filePath = tempDirectory.resolve("mealplans.json");
        final FileMealPlanDataAccessObject dataAccess = new FileMealPlanDataAccessObject(filePath);
        final MealPlan plan = new MealPlan(WEEK_A);
        plan.assign(MONDAY_DINNER, pastaMeal());
        dataAccess.saveMealPlan(plan);
        final RecordingMovePresenter presenter = new RecordingMovePresenter();
        final MovePlannedMealInteractor interactor =
                new MovePlannedMealInteractor(dataAccess, presenter);

        interactor.execute(new MovePlannedMealInputData(WEEK_A,
                DayOfWeek.MONDAY, MealType.DINNER, DayOfWeek.TUESDAY, MealType.BREAKFAST));

        final MealPlan reloaded = new FileMealPlanDataAccessObject(filePath).getMealPlan(WEEK_A);
        assertNull(presenter.errorMessage);
        assertEquals(List.of(TUESDAY_BREAKFAST), List.copyOf(reloaded.getMeals().keySet()));
    }

    private static void assertSameIngredients(List<RecipeIngredient> expected,
                                              List<RecipeIngredient> actual) {
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals(expected.get(index).getName(), actual.get(index).getName());
            assertEquals(expected.get(index).getDisplayName(), actual.get(index).getDisplayName());
            assertEquals(expected.get(index).getMeasureDescription(),
                    actual.get(index).getMeasureDescription());
        }
    }

    private static PlannedMeal pastaMeal() {
        return new PlannedMeal("52771", "Spicy Arrabiata Penne", List.of(
                ingredient("penne rigate", "1 pound"),
                ingredient("Olive_Oil", "1/4 cup"),
                ingredient("garlic", "3 cloves")));
    }

    private static PlannedMeal pancakesMeal() {
        return new PlannedMeal("3", "Pancakes", List.of(
                ingredient("Flour", "1 cup"),
                ingredient("Eggs", "2")));
    }

    private static RecipeIngredient ingredient(String name, String measure) {
        return new RecipeIngredient(IngredientName.fromRawName(name), measure);
    }

    private static final class RecordingMovePresenter implements MovePlannedMealOutputBoundary {
        private String errorMessage;

        @Override
        public void prepareSuccessView(MovePlannedMealOutputData outputData) {
            // Only failures matter for this test.
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
