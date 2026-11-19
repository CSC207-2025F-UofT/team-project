package use_case.update_stock_level;

import entity.IngredientName;
import entity.Pantry;
import entity.PantryItem;
import entity.StockLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateStockLevelInteractorTest {

    @Test
    void changesEnoughToLowAndSaves() {
        final InMemoryDataAccess dataAccess = dataAccessWith("Milk", StockLevel.ENOUGH);
        final FakePresenter presenter = new FakePresenter();
        final UpdateStockLevelInteractor interactor =
                new UpdateStockLevelInteractor(dataAccess, presenter);

        interactor.execute(new UpdateStockLevelInputData("milk", StockLevel.LOW));

        assertFalse(presenter.outputData.isUseCaseFailed());
        assertEquals("Milk", presenter.outputData.getIngredientName());
        assertEquals(StockLevel.LOW, presenter.outputData.getStockLevel());
        assertEquals("", presenter.outputData.getMessage());
        assertEquals(1, dataAccess.saveCount);
        assertEquals(StockLevel.LOW, dataAccess.pantry.find(
                IngredientName.fromRawName("Milk")).orElseThrow().getStockLevel());
    }

    @Test
    void changesLowToEnoughAndSaves() {
        final InMemoryDataAccess dataAccess = dataAccessWith("Garlic", StockLevel.LOW);
        final FakePresenter presenter = new FakePresenter();
        final UpdateStockLevelInteractor interactor =
                new UpdateStockLevelInteractor(dataAccess, presenter);

        interactor.execute(new UpdateStockLevelInputData("Garlic", StockLevel.ENOUGH));

        assertFalse(presenter.outputData.isUseCaseFailed());
        assertEquals(StockLevel.ENOUGH, dataAccess.pantry.find(
                IngredientName.fromRawName("Garlic")).orElseThrow().getStockLevel());
        assertEquals(1, dataAccess.saveCount);
    }

    @Test
    void failsWhenIngredientIsMissing() {
        final InMemoryDataAccess dataAccess = new InMemoryDataAccess(new Pantry());
        final FakePresenter presenter = new FakePresenter();
        final UpdateStockLevelInteractor interactor =
                new UpdateStockLevelInteractor(dataAccess, presenter);

        interactor.execute(new UpdateStockLevelInputData("Garlic", StockLevel.LOW));

        assertTrue(presenter.outputData.isUseCaseFailed());
        assertEquals("Ingredient is not in the pantry.", presenter.outputData.getMessage());
        assertNull(presenter.outputData.getIngredientName());
        assertEquals(0, dataAccess.saveCount);
    }

    @Test
    void failsForInvalidInput() {
        final InMemoryDataAccess dataAccess = dataAccessWith("Garlic", StockLevel.ENOUGH);
        final FakePresenter presenter = new FakePresenter();
        final UpdateStockLevelInteractor interactor =
                new UpdateStockLevelInteractor(dataAccess, presenter);

        interactor.execute(new UpdateStockLevelInputData("   ", StockLevel.LOW));

        assertTrue(presenter.outputData.isUseCaseFailed());
        assertEquals("Ingredient name and stock level are required.",
                presenter.outputData.getMessage());
        assertEquals(0, dataAccess.saveCount);
    }

    private InMemoryDataAccess dataAccessWith(String name, StockLevel stockLevel) {
        final Pantry pantry = new Pantry();
        pantry.add(new PantryItem(
                IngredientName.fromRawName(name), null, stockLevel));
        return new InMemoryDataAccess(pantry);
    }

    private static final class InMemoryDataAccess
            implements UpdateStockLevelDataAccessInterface {
        private Pantry pantry;
        private int saveCount;

        private InMemoryDataAccess(Pantry pantry) {
            this.pantry = pantry;
        }

        @Override
        public Pantry loadPantry() {
            return pantry;
        }

        @Override
        public void savePantry(Pantry updatedPantry) {
            pantry = updatedPantry;
            saveCount++;
        }
    }

    private static final class FakePresenter implements UpdateStockLevelOutputBoundary {
        private UpdateStockLevelOutputData outputData;

        @Override
        public void prepareSuccessView(UpdateStockLevelOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(UpdateStockLevelOutputData outputData) {
            this.outputData = outputData;
        }
    }
}
