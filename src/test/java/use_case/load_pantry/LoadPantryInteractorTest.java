package use_case.load_pantry;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import entity.IngredientName;
import entity.Pantry;
import entity.PantryItem;
import entity.StockLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadPantryInteractorTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 11, 18);
    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-11-18T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void loadsItemsOrderedByExpiryWithDisplayStatus() {
        Pantry pantry = new Pantry();
        pantry.add(item("No expiry", null, StockLevel.ENOUGH));
        pantry.add(item("Later", TODAY.plusDays(4), StockLevel.ENOUGH));
        pantry.add(item("Soon", TODAY.plusDays(3), StockLevel.LOW));
        pantry.add(item("Expired", TODAY.minusDays(1), StockLevel.ENOUGH));

        FakePresenter presenter = new FakePresenter();
        LoadPantryInteractor interactor = new LoadPantryInteractor(
                new InMemoryDataAccess(pantry), presenter, FIXED_CLOCK);

        interactor.execute(new LoadPantryInputData());

        List<LoadPantryItemOutputData> items = presenter.outputData.getItems();
        assertEquals(List.of("Expired", "Soon", "Later", "No expiry"),
                items.stream().map(LoadPantryItemOutputData::getDisplayName).toList());

        assertTrue(items.get(0).isExpired());
        assertFalse(items.get(0).isExpiringSoon());

        assertFalse(items.get(1).isExpired());
        assertTrue(items.get(1).isExpiringSoon());
        assertEquals("soon", items.get(1).getIngredientKey());
        assertEquals(TODAY.plusDays(3), items.get(1).getExpiryDate());
        assertEquals(StockLevel.LOW, items.get(1).getStockLevel());

        assertFalse(items.get(2).isExpired());
        assertFalse(items.get(2).isExpiringSoon());
        assertNull(items.get(3).getExpiryDate());
        assertFalse(presenter.outputData.isEmpty());
    }

    @Test
    void presentsAnEmptyPantry() {
        FakePresenter presenter = new FakePresenter();
        LoadPantryInteractor interactor = new LoadPantryInteractor(
                new InMemoryDataAccess(new Pantry()), presenter, FIXED_CLOCK);

        interactor.execute(new LoadPantryInputData());

        assertTrue(presenter.outputData.isEmpty());
        assertTrue(presenter.outputData.getItems().isEmpty());
    }

    @Test
    void rejectsNullInputData() {
        LoadPantryInteractor interactor = new LoadPantryInteractor(
                new InMemoryDataAccess(new Pantry()), outputData -> { }, FIXED_CLOCK);

        assertThrows(NullPointerException.class, () -> interactor.execute(null));
    }

    private static PantryItem item(String name, LocalDate expiryDate, StockLevel stockLevel) {
        return new PantryItem(IngredientName.fromRawName(name), expiryDate, stockLevel);
    }

    private static final class InMemoryDataAccess implements LoadPantryDataAccessInterface {
        private final Pantry pantry;

        private InMemoryDataAccess(Pantry pantry) {
            this.pantry = pantry;
        }

        @Override
        public Pantry loadPantry() {
            return pantry;
        }
    }

    private static final class FakePresenter implements LoadPantryOutputBoundary {
        private LoadPantryOutputData outputData;

        @Override
        public void prepareSuccessView(LoadPantryOutputData outputData) {
            this.outputData = outputData;
        }
    }
}
