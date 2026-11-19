package use_case.toggle_bought;

import entity.IngredientName;
import entity.ShoppingItem;
import entity.ShoppingList;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleBoughtInteractorTest {

    private final LocalDate monday = LocalDate.of(2026, 11, 16);

    @Test
    void togglesBoughtAndSaves() {
        ShoppingItem item = new ShoppingItem(
                IngredientName.fromRawName("Garlic"),
                ShoppingItem.Reason.MISSING,
                Set.of("Pasta"));
        InMemoryDao dao = new InMemoryDao();
        dao.lists.put(monday, new ShoppingList(monday, List.of(item)));

        FakePresenter presenter = new FakePresenter();
        ToggleBoughtInteractor interactor = new ToggleBoughtInteractor(dao, presenter);
        interactor.execute(new ToggleBoughtInputData(monday, "Garlic"));

        assertFalse(presenter.failed);
        assertFalse(presenter.output.isUseCaseFailed());
        assertEquals("", presenter.output.getMessage());
        assertTrue(presenter.output.getShoppingList().find(IngredientName.fromRawName("Garlic")).isBought());
        assertTrue(dao.lists.get(monday).find(IngredientName.fromRawName("Garlic")).isBought());
    }

    @Test
    void failsWhenListMissing() {
        FakePresenter presenter = new FakePresenter();
        ToggleBoughtInteractor interactor =
                new ToggleBoughtInteractor(new InMemoryDao(), presenter);
        interactor.execute(new ToggleBoughtInputData(monday, "Garlic"));

        assertTrue(presenter.failed);
        assertEquals("No shopping list is saved for this week.", presenter.output.getMessage());
    }

    @Test
    void failsWhenItemMissing() {
        InMemoryDao dao = new InMemoryDao();
        dao.lists.put(monday, new ShoppingList(monday, List.of()));
        FakePresenter presenter = new FakePresenter();
        ToggleBoughtInteractor interactor = new ToggleBoughtInteractor(dao, presenter);
        interactor.execute(new ToggleBoughtInputData(monday, "Garlic"));

        assertTrue(presenter.failed);
        assertEquals("Item is not on the shopping list.", presenter.output.getMessage());
    }

    private static class InMemoryDao implements ToggleBoughtDataAccessInterface {
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

    private static class FakePresenter implements ToggleBoughtOutputBoundary {
        private ToggleBoughtOutputData output;
        private boolean failed;

        @Override
        public void prepareSuccessView(ToggleBoughtOutputData outputData) {
            this.output = outputData;
            this.failed = false;
        }

        @Override
        public void prepareFailView(ToggleBoughtOutputData outputData) {
            this.output = outputData;
            this.failed = true;
        }
    }
}
