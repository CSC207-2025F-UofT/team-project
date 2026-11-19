package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import entity.IngredientName;
import entity.Pantry;
import entity.PantryItem;
import entity.StockLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.update_stock_level.UpdateStockLevelInputData;
import use_case.update_stock_level.UpdateStockLevelInteractor;
import use_case.update_stock_level.UpdateStockLevelOutputBoundary;
import use_case.update_stock_level.UpdateStockLevelOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilePantryDataAccessObjectTest {
    @TempDir
    private Path tempDirectory;

    @Test
    void createsMissingFileAndLoadsEmptyPantry() {
        final Path filePath = tempDirectory.resolve("nested/pantry.json");

        final FilePantryDataAccessObject dataAccess =
                new FilePantryDataAccessObject(filePath);

        assertTrue(Files.exists(filePath));
        assertTrue(dataAccess.loadPantry().sortedByExpiry().isEmpty());
    }

    @Test
    void savesAndLoadsEveryPantryItemField() {
        final Path filePath = tempDirectory.resolve("pantry.json");
        final FilePantryDataAccessObject writer =
                new FilePantryDataAccessObject(filePath);
        final Pantry pantry = new Pantry();
        pantry.add(item("Chicken_Breast", LocalDate.of(2026, 11, 21), StockLevel.LOW));
        pantry.add(item("Garlic", null, StockLevel.ENOUGH));

        writer.savePantry(pantry);

        final FilePantryDataAccessObject reader =
                new FilePantryDataAccessObject(filePath);
        final Pantry loaded = reader.loadPantry();
        final PantryItem chicken = loaded.find(
                IngredientName.fromRawName("chicken breast")).orElseThrow();
        final PantryItem garlic = loaded.find(
                IngredientName.fromRawName("garlic")).orElseThrow();

        assertEquals("Chicken_Breast", chicken.getName().getDisplayName());
        assertEquals(LocalDate.of(2026, 11, 21), chicken.getExpiryDate());
        assertEquals(StockLevel.LOW, chicken.getStockLevel());
        assertNull(garlic.getExpiryDate());
        assertEquals(StockLevel.ENOUGH, garlic.getStockLevel());
        assertEquals(2, loaded.sortedByExpiry().size());
    }

    @Test
    void pantryReaderReturnsPersistedPantry() {
        final FilePantryDataAccessObject dataAccess =
                new FilePantryDataAccessObject(tempDirectory.resolve("pantry.json"));
        final Pantry pantry = new Pantry();
        pantry.add(item("Onion", null, StockLevel.LOW));
        dataAccess.savePantry(pantry);

        final Pantry loaded = dataAccess.getPantry();

        assertFalse(loaded.sortedByExpiry().isEmpty());
        assertEquals(StockLevel.LOW, loaded.find(
                IngredientName.fromRawName("onion")).orElseThrow().getStockLevel());
    }

    @Test
    void blankFileLoadsAsEmptyPantry() throws IOException {
        final Path filePath = tempDirectory.resolve("pantry.json");
        Files.writeString(filePath, "   ", StandardCharsets.UTF_8);

        final FilePantryDataAccessObject dataAccess =
                new FilePantryDataAccessObject(filePath);

        assertTrue(dataAccess.loadPantry().sortedByExpiry().isEmpty());
    }

    @Test
    void rejectsMalformedPantryFile() throws IOException {
        final Path filePath = tempDirectory.resolve("pantry.json");
        Files.writeString(filePath, "{not valid json", StandardCharsets.UTF_8);
        final FilePantryDataAccessObject dataAccess =
                new FilePantryDataAccessObject(filePath);

        assertThrows(IllegalStateException.class, dataAccess::loadPantry);
    }

    @Test
    void rejectsPantryFileWithWrongItemsType() throws IOException {
        final Path filePath = tempDirectory.resolve("pantry.json");
        Files.writeString(filePath, "{\"items\": \"not an array\"}", StandardCharsets.UTF_8);
        final FilePantryDataAccessObject dataAccess =
                new FilePantryDataAccessObject(filePath);

        assertThrows(IllegalStateException.class, dataAccess::loadPantry);
    }

    @Test
    void rejectsNullPantryOnSave() {
        final FilePantryDataAccessObject dataAccess =
                new FilePantryDataAccessObject(tempDirectory.resolve("pantry.json"));

        assertThrows(NullPointerException.class, () -> dataAccess.savePantry(null));
    }

    @Test
    void updateStockLevelUseCasePersistsThroughFileDao() {
        final Path filePath = tempDirectory.resolve("pantry.json");
        final FilePantryDataAccessObject dataAccess =
                new FilePantryDataAccessObject(filePath);
        final Pantry pantry = new Pantry();
        pantry.add(item("Milk", null, StockLevel.ENOUGH));
        dataAccess.savePantry(pantry);
        final RecordingPresenter presenter = new RecordingPresenter();
        final UpdateStockLevelInteractor interactor =
                new UpdateStockLevelInteractor(dataAccess, presenter);

        interactor.execute(new UpdateStockLevelInputData("milk", StockLevel.LOW));

        final Pantry reloaded = new FilePantryDataAccessObject(filePath).loadPantry();
        assertFalse(presenter.outputData.isUseCaseFailed());
        assertEquals(StockLevel.LOW, reloaded.find(
                IngredientName.fromRawName("Milk")).orElseThrow().getStockLevel());
    }

    private PantryItem item(String name, LocalDate expiryDate, StockLevel stockLevel) {
        return new PantryItem(IngredientName.fromRawName(name), expiryDate, stockLevel);
    }

    private static final class RecordingPresenter implements UpdateStockLevelOutputBoundary {
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
