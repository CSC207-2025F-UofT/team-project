package use_case.load_shopping_list;

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

class LoadShoppingListInteractorTest {

    private final LocalDate monday = LocalDate.of(2026, 11, 16);

    @Test
    void returnsSavedListWhenPresent() {
        ShoppingItem item = new ShoppingItem(
                IngredientName.fromRawName("Garlic"),
                ShoppingItem.Reason.MISSING,
                Set.of("Pasta"));
        ShoppingList saved = new ShoppingList(monday, List.of(item));
        InMemoryDao dao = new InMemoryDao();
        dao.lists.put(monday, saved);

        FakePresenter presenter = new FakePresenter();
        LoadShoppingListInteractor interactor = new LoadShoppingListInteractor(dao, presenter);
        interactor.execute(new LoadShoppingListInputData(monday));

        assertFalse(presenter.output.isEmpty());
        assertEquals(1, presenter.output.getShoppingList().getItems().size());
        assertEquals("Garlic",
                presenter.output.getShoppingList().getItems().get(0).getName().getDisplayName());
    }

    @Test
    void returnsEmptyListWhenNothingSaved() {
        FakePresenter presenter = new FakePresenter();
        LoadShoppingListInteractor interactor =
                new LoadShoppingListInteractor(new InMemoryDao(), presenter);
        interactor.execute(new LoadShoppingListInputData(monday));

        assertTrue(presenter.output.isEmpty());
        assertTrue(presenter.output.getShoppingList().getItems().isEmpty());
        assertEquals(monday, presenter.output.getShoppingList().getWeekStart());
    }

    private static class InMemoryDao implements LoadShoppingListDataAccessInterface {
        private final Map<LocalDate, ShoppingList> lists = new HashMap<>();

        @Override
        public ShoppingList loadShoppingList(LocalDate weekStart) {
            return lists.get(weekStart);
        }
    }

    private static class FakePresenter implements LoadShoppingListOutputBoundary {
        private LoadShoppingListOutputData output;

        @Override
        public void prepareSuccessView(LoadShoppingListOutputData outputData) {
            this.output = outputData;
        }
    }
}
