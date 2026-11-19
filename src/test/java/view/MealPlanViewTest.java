package view;

import app.MealPlanDemo;
import data_access.InMemoryMealPlanDataAccessObject;
import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Drives the meal plan page through its buttons, with an in-memory DAO.
 */
class MealPlanViewTest {

    private static final LocalDate THIS_MONDAY =
            LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    private InMemoryMealPlanDataAccessObject dao;
    private MealPlanView view;

    /** Cell index in the grid: rows are breakfast, lunch, dinner; columns Monday to Sunday. */
    private static int cell(DayOfWeek day, MealType mealType) {
        return mealType.ordinal() * 7 + day.ordinal();
    }

    private JButton button(DayOfWeek day, MealType mealType) {
        return view.getCellButtons().get(cell(day, mealType));
    }

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        dao = new InMemoryMealPlanDataAccessObject();
        MealPlan plan = new MealPlan(THIS_MONDAY);
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER), new PlannedMeal("1", "Pasta",
                List.of(new RecipeIngredient(IngredientName.fromRawName("Garlic"), "3 cloves"))));
        PlannedMeal pancakes = new PlannedMeal("2", "Pancakes", List.of());
        pancakes.markCooked();
        plan.assign(new MealSlot(DayOfWeek.TUESDAY, MealType.BREAKFAST), pancakes);
        dao.saveMealPlan(plan);
        view = MealPlanDemo.buildView(dao);
        view.onShown();
    }

    @Test
    void showsTheWeekInTheGrid() {
        assertEquals(21, view.getCellButtons().size());
        assertTrue(button(DayOfWeek.MONDAY, MealType.DINNER).getText().contains("Pasta"));
        assertTrue(button(DayOfWeek.TUESDAY, MealType.BREAKFAST).getText().contains("cooked"));
        assertEquals("-", button(DayOfWeek.SUNDAY, MealType.LUNCH).getText());
    }

    @Test
    void selectingAMealShowsItsIngredients() {
        button(DayOfWeek.MONDAY, MealType.DINNER).doClick();

        assertEquals("Pasta", view.getSelectedTitle().getText());
        assertTrue(view.getIngredientsArea().getText().contains("3 cloves"));
        assertTrue(view.getRemoveButton().isEnabled());
        assertTrue(view.getMoveButton().isEnabled());
    }

    @Test
    void emptySlotCannotBeRemovedOrMoved() {
        button(DayOfWeek.SUNDAY, MealType.LUNCH).doClick();

        assertEquals("Empty slot", view.getSelectedTitle().getText());
        assertFalse(view.getRemoveButton().isEnabled());
        assertFalse(view.getMoveButton().isEnabled());
    }

    @Test
    void removeClearsTheCellAndSaves() {
        button(DayOfWeek.MONDAY, MealType.DINNER).doClick();
        view.getRemoveButton().doClick();

        assertEquals("-", button(DayOfWeek.MONDAY, MealType.DINNER).getText());
        assertEquals("Removed Pasta.", view.getMessageLabel().getText());
        assertFalse(dao.getMealPlan(THIS_MONDAY).getMeals()
                .containsKey(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER)));
    }

    @Test
    void moveToEmptySlotMovesTheMealAndSelectsIt() {
        button(DayOfWeek.MONDAY, MealType.DINNER).doClick();
        view.getMoveButton().doClick();
        assertTrue(view.getMessageLabel().getText().startsWith("Click an empty slot"));
        button(DayOfWeek.SATURDAY, MealType.LUNCH).doClick();

        assertEquals("-", button(DayOfWeek.MONDAY, MealType.DINNER).getText());
        assertTrue(button(DayOfWeek.SATURDAY, MealType.LUNCH).getText().contains("Pasta"));
        assertEquals("Pasta", view.getSelectedTitle().getText());
        assertEquals("Moved Pasta.", view.getMessageLabel().getText());
        assertTrue(dao.getMealPlan(THIS_MONDAY).getMeals()
                .containsKey(new MealSlot(DayOfWeek.SATURDAY, MealType.LUNCH)));
    }

    @Test
    void cancelMoveLeavesTheWeekUnchanged() {
        button(DayOfWeek.MONDAY, MealType.DINNER).doClick();
        view.getMoveButton().doClick();
        assertTrue(view.getCancelMoveButton().isVisible());
        assertFalse(view.getMoveButton().isVisible());

        view.getCancelMoveButton().doClick();
        button(DayOfWeek.SUNDAY, MealType.LUNCH).doClick();

        assertFalse(view.getCancelMoveButton().isVisible());
        assertTrue(view.getMoveButton().isVisible());
        assertEquals("-", button(DayOfWeek.SUNDAY, MealType.LUNCH).getText());
        assertTrue(button(DayOfWeek.MONDAY, MealType.DINNER).getText().contains("Pasta"));
    }

    @Test
    void moveToTakenSlotShowsError() {
        button(DayOfWeek.MONDAY, MealType.DINNER).doClick();
        view.getMoveButton().doClick();
        button(DayOfWeek.TUESDAY, MealType.BREAKFAST).doClick();

        assertFalse(view.getErrorLabel().getText().isBlank());
        assertTrue(button(DayOfWeek.MONDAY, MealType.DINNER).getText().contains("Pasta"));
    }

    @Test
    void cookedMealCannotBeMoved() {
        button(DayOfWeek.TUESDAY, MealType.BREAKFAST).doClick();
        view.getMoveButton().doClick();
        button(DayOfWeek.SUNDAY, MealType.DINNER).doClick();

        assertTrue(view.getErrorLabel().getText().contains("cooked"));
        assertTrue(button(DayOfWeek.TUESDAY, MealType.BREAKFAST).getText().contains("Pancakes"));
    }

    @Test
    void emptyWeekShowsRecipesHint() {
        InMemoryMealPlanDataAccessObject emptyDao = new InMemoryMealPlanDataAccessObject();
        MealPlanView emptyView = MealPlanDemo.buildView(emptyDao);
        emptyView.onShown();

        assertTrue(emptyView.getMessageLabel().getText().contains("Recipes page"));
    }
}
