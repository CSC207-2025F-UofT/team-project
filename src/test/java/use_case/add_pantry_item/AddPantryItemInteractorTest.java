package use_case.add_pantry_item;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import entity.IngredientName;
import entity.Pantry;
import entity.PantryItem;
import entity.StockLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddPantryItemInteractorTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 11, 23);
    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-11-23T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void addsItemWithEnoughStockAndSaves() {
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(new Pantry());
        final FakePresenter presenter = new FakePresenter();
        final AddPantryItemInteractor interactor =
                new AddPantryItemInteractor(dataAccess, presenter, FIXED_CLOCK);

        interactor.execute(new AddPantryItemInputData("Fresh Milk", TODAY.plusDays(5)));

        assertFalse(presenter.outputData.isUseCaseFailed());
        assertEquals("Fresh Milk", presenter.outputData.getIngredientName());
        assertEquals(TODAY.plusDays(5), presenter.outputData.getExpiryDate());
        assertEquals(StockLevel.ENOUGH, presenter.outputData.getStockLevel());
        assertEquals("", presenter.outputData.getMessage());
        assertEquals(1, dataAccess.saveCount);

        final PantryItem savedItem = dataAccess.pantry.find(
                IngredientName.fromRawName("fresh milk")).orElseThrow();
        assertEquals(TODAY.plusDays(5), savedItem.getExpiryDate());
        assertEquals(StockLevel.ENOUGH, savedItem.getStockLevel());
    }

    @Test
    void addsItemWithoutExpiryDate() {
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(new Pantry());
        final FakePresenter presenter = new FakePresenter();
        final AddPantryItemInteractor interactor =
                new AddPantryItemInteractor(dataAccess, presenter, FIXED_CLOCK);

        interactor.execute(new AddPantryItemInputData("Garlic", null));

        assertFalse(presenter.outputData.isUseCaseFailed());
        assertNull(presenter.outputData.getExpiryDate());
        assertEquals(1, dataAccess.saveCount);
    }

    @Test
    void acceptsExpiryDateToday() {
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(new Pantry());
        final FakePresenter presenter = new FakePresenter();
        final AddPantryItemInteractor interactor =
                new AddPantryItemInteractor(dataAccess, presenter, FIXED_CLOCK);

        interactor.execute(new AddPantryItemInputData("Bread", TODAY));

        assertFalse(presenter.outputData.isUseCaseFailed());
        assertEquals(1, dataAccess.saveCount);
    }

    @Test
    void rejectsDuplicateIngredientWithoutSaving() {
        final Pantry pantry = new Pantry();
        pantry.add(new PantryItem(
                IngredientName.fromRawName("Garlic"), null, StockLevel.ENOUGH));
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(pantry);
        final FakePresenter presenter = new FakePresenter();
        final AddPantryItemInteractor interactor =
                new AddPantryItemInteractor(dataAccess, presenter, FIXED_CLOCK);

        interactor.execute(new AddPantryItemInputData(" garlic ", TODAY.plusDays(2)));

        assertTrue(presenter.outputData.isUseCaseFailed());
        assertEquals("Ingredient is already in the pantry.",
                presenter.outputData.getMessage());
        assertEquals(0, dataAccess.saveCount);
    }

    @Test
    void rejectsPastExpiryDateWithoutLoadingOrSaving() {
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(new Pantry());
        final FakePresenter presenter = new FakePresenter();
        final AddPantryItemInteractor interactor =
                new AddPantryItemInteractor(dataAccess, presenter, FIXED_CLOCK);

        interactor.execute(new AddPantryItemInputData("Milk", TODAY.minusDays(1)));

        assertTrue(presenter.outputData.isUseCaseFailed());
        assertEquals("Expiry date cannot be in the past.",
                presenter.outputData.getMessage());
        assertEquals(0, dataAccess.loadCount);
        assertEquals(0, dataAccess.saveCount);
    }

    @Test
    void rejectsBlankNameWithoutSaving() {
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(new Pantry());
        final FakePresenter presenter = new FakePresenter();
        final AddPantryItemInteractor interactor =
                new AddPantryItemInteractor(dataAccess, presenter, FIXED_CLOCK);

        interactor.execute(new AddPantryItemInputData("   ", null));

        assertTrue(presenter.outputData.isUseCaseFailed());
        assertEquals("Ingredient name is required.", presenter.outputData.getMessage());
        assertEquals(0, dataAccess.saveCount);
    }

    @Test
    void rejectsNullInputWithoutSaving() {
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(new Pantry());
        final FakePresenter presenter = new FakePresenter();
        final AddPantryItemInteractor interactor =
                new AddPantryItemInteractor(dataAccess, presenter, FIXED_CLOCK);

        interactor.execute(null);

        assertTrue(presenter.outputData.isUseCaseFailed());
        assertEquals("Ingredient name is required.", presenter.outputData.getMessage());
        assertEquals(0, dataAccess.saveCount);
    }

    private static final class InMemoryDataAccess
            implements AddPantryItemDataAccessInterface {
        private Pantry pantry;
        private int loadCount;
        private int saveCount;

        private InMemoryDataAccess(Pantry pantry) {
            this.pantry = pantry;
        }

        @Override
        public Pantry loadPantry() {
            loadCount++;
            return pantry;
        }

        @Override
        public void savePantry(Pantry updatedPantry) {
            pantry = updatedPantry;
            saveCount++;
        }
    }

    private static final class FakePresenter implements AddPantryItemOutputBoundary {
        private AddPantryItemOutputData outputData;

        @Override
        public void prepareSuccessView(AddPantryItemOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(AddPantryItemOutputData outputData) {
            this.outputData = outputData;
        }
    }
}
