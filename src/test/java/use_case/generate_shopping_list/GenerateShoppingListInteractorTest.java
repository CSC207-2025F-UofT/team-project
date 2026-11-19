package use_case.generate_shopping_list;

import entity.IngredientName;
import entity.MealPlan;
import entity.MealSlot;
import entity.MealType;
import entity.Pantry;
import entity.PantryItem;
import entity.PlannedMeal;
import entity.RecipeIngredient;
import entity.ShoppingItem;
import entity.ShoppingList;
import entity.StockLevel;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerateShoppingListInteractorTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 18);
    private static final LocalDate MONDAY = LocalDate.of(2026, 11, 16);
    private static final Clock CLOCK = Clock.fixed(
            Instant.parse("2026-11-18T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void failsWhenNothingUncookedIsPlanned() {
        FakePresenter presenter = new FakePresenter();
        GenerateShoppingListInteractor interactor = newInteractor(
                null, new Pantry(), new InMemoryShoppingDao(), presenter);

        interactor.execute(new GenerateShoppingListInputData());

        assertTrue(presenter.failed);
        assertTrue(presenter.output.isUseCaseFailed());
        assertEquals("Nothing planned to shop for.", presenter.output.getMessage());
    }

    @Test
    void failsWhenAllMealsAlreadyCooked() {
        MealPlan plan = new MealPlan(MONDAY);
        PlannedMeal meal = pastaMeal();
        meal.markCooked();
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER), meal);

        FakePresenter presenter = new FakePresenter();
        GenerateShoppingListInteractor interactor = newInteractor(
                plan, new Pantry(), new InMemoryShoppingDao(), presenter);
        interactor.execute(new GenerateShoppingListInputData());

        assertTrue(presenter.failed);
        assertEquals("Nothing planned to shop for.", presenter.output.getMessage());
    }

    @Test
    void classifiesMissingExpiredLowAndSkipsEnough() {
        MealPlan plan = new MealPlan(MONDAY);
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER), pastaMeal());
        plan.assign(new MealSlot(DayOfWeek.TUESDAY, MealType.LUNCH), soupMeal());

        Pantry pantry = new Pantry();
        pantry.add(new PantryItem(IngredientName.fromRawName("Olive Oil"),
                TODAY.plusDays(10), StockLevel.ENOUGH));
        pantry.add(new PantryItem(IngredientName.fromRawName("Garlic"),
                TODAY.minusDays(1), StockLevel.ENOUGH));
        pantry.add(new PantryItem(IngredientName.fromRawName("Tomato"),
                TODAY.plusDays(5), StockLevel.LOW));

        InMemoryShoppingDao dao = new InMemoryShoppingDao();
        FakePresenter presenter = new FakePresenter();
        GenerateShoppingListInteractor interactor = newInteractor(plan, pantry, dao, presenter);
        interactor.execute(new GenerateShoppingListInputData());

        assertFalse(presenter.failed);
        assertFalse(presenter.output.isUseCaseFailed());
        List<ShoppingItem> items = presenter.output.getShoppingList().getItems();
        assertEquals(3, items.size());
        assertEquals(ShoppingItem.Reason.MISSING, items.get(0).getReason());
        assertEquals("Garlic", items.get(0).getName().getDisplayName());
        assertEquals(ShoppingItem.Reason.MISSING, items.get(1).getReason());
        assertEquals("Onion", items.get(1).getName().getDisplayName());
        assertEquals(ShoppingItem.Reason.LOW, items.get(2).getReason());
        assertEquals("Tomato", items.get(2).getName().getDisplayName());
        assertEquals(Set.of("Pasta", "Soup"), items.get(0).getNeededFor());
        assertTrue(dao.lists.containsKey(MONDAY));
    }

    @Test
    void succeedsWithEmptyListWhenEverythingIsInFridge() {
        MealPlan plan = new MealPlan(MONDAY);
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER), pastaMeal());

        Pantry pantry = new Pantry();
        pantry.add(new PantryItem(IngredientName.fromRawName("Garlic"),
                TODAY.plusDays(3), StockLevel.ENOUGH));
        pantry.add(new PantryItem(IngredientName.fromRawName("Olive Oil"),
                TODAY.plusDays(3), StockLevel.ENOUGH));
        pantry.add(new PantryItem(IngredientName.fromRawName("Tomato"),
                TODAY.plusDays(3), StockLevel.ENOUGH));

        FakePresenter presenter = new FakePresenter();
        GenerateShoppingListInteractor interactor =
                newInteractor(plan, pantry, new InMemoryShoppingDao(), presenter);
        interactor.execute(new GenerateShoppingListInputData());

        assertFalse(presenter.failed);
        assertTrue(presenter.output.getShoppingList().getItems().isEmpty());
        assertEquals("You have everything you need.", presenter.output.getMessage());
    }

    @Test
    void keepsBoughtTicksWhenRegeneratingAndDropsRemovedItems() {
        MealPlan plan = new MealPlan(MONDAY);
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER), pastaMeal());

        ShoppingItem oldGarlic = new ShoppingItem(
                IngredientName.fromRawName("Garlic"),
                ShoppingItem.Reason.MISSING,
                Set.of("Pasta"));
        oldGarlic.setBought(true);
        ShoppingItem oldSalt = new ShoppingItem(
                IngredientName.fromRawName("Salt"),
                ShoppingItem.Reason.MISSING,
                Set.of("Old Recipe"));
        oldSalt.setBought(true);
        InMemoryShoppingDao dao = new InMemoryShoppingDao();
        dao.lists.put(MONDAY, new ShoppingList(MONDAY, List.of(oldGarlic, oldSalt)));

        FakePresenter presenter = new FakePresenter();
        GenerateShoppingListInteractor interactor =
                newInteractor(plan, new Pantry(), dao, presenter);
        interactor.execute(new GenerateShoppingListInputData());

        Map<String, ShoppingItem> byName = new HashMap<>();
        for (ShoppingItem item : presenter.output.getShoppingList().getItems()) {
            byName.put(item.getName().getKey(), item);
        }
        assertTrue(byName.get("garlic").isBought());
        assertFalse(byName.get("olive oil").isBought());
        assertNull(byName.get("salt"));
    }

    @Test
    void mergesSameIngredientAcrossRecipesByIngredientName() {
        MealPlan plan = new MealPlan(MONDAY);
        plan.assign(new MealSlot(DayOfWeek.MONDAY, MealType.DINNER), pastaMeal());
        plan.assign(new MealSlot(DayOfWeek.TUESDAY, MealType.LUNCH), soupMeal());

        FakePresenter presenter = new FakePresenter();
        GenerateShoppingListInteractor interactor =
                newInteractor(plan, new Pantry(), new InMemoryShoppingDao(), presenter);
        interactor.execute(new GenerateShoppingListInputData());

        ShoppingItem garlic = presenter.output.getShoppingList()
                .find(IngredientName.fromRawName("garlic"));
        assertEquals(Set.of("Pasta", "Soup"), garlic.getNeededFor());
    }

    private GenerateShoppingListInteractor newInteractor(MealPlan plan,
                                                         Pantry pantry,
                                                         InMemoryShoppingDao dao,
                                                         FakePresenter presenter) {
        return new GenerateShoppingListInteractor(
                weekStart -> plan,
                () -> pantry,
                dao,
                new DefaultShoppingNeedPolicy(),
                presenter,
                CLOCK);
    }

    private static PlannedMeal pastaMeal() {
        return new PlannedMeal("1", "Pasta", List.of(
                new RecipeIngredient(IngredientName.fromRawName("Garlic"), "2 cloves"),
                new RecipeIngredient(IngredientName.fromRawName("Olive Oil"), "1/4 cup"),
                new RecipeIngredient(IngredientName.fromRawName("Tomato"), "2")));
    }

    private static PlannedMeal soupMeal() {
        return new PlannedMeal("2", "Soup", List.of(
                new RecipeIngredient(IngredientName.fromRawName("garlic"), "1 clove"),
                new RecipeIngredient(IngredientName.fromRawName("Onion"), "1")));
    }

    private static class InMemoryShoppingDao implements GenerateShoppingListDataAccessInterface {
        private final Map<LocalDate, ShoppingList> lists = new HashMap<>();

        @Override
        public ShoppingList loadShoppingList(LocalDate weekStart) {
            return lists.get(weekStart);
        }

        @Override
        public void saveShoppingList(ShoppingList shoppingList) {
            lists.put(shoppingList.getWeekStart(), shoppingList);
        }
    }

    private static class FakePresenter implements GenerateShoppingListOutputBoundary {
        private GenerateShoppingListOutputData output;
        private boolean failed;

        @Override
        public void prepareSuccessView(GenerateShoppingListOutputData outputData) {
            this.output = outputData;
            this.failed = false;
        }

        @Override
        public void prepareFailView(GenerateShoppingListOutputData outputData) {
            this.output = outputData;
            this.failed = true;
        }
    }
}
