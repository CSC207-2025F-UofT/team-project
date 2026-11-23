package data_access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import entity.IngredientName;
import entity.Pantry;
import entity.PantryItem;
import entity.StockLevel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.add_pantry_item.AddPantryItemDataAccessInterface;
import use_case.generate_shopping_list.PantryReader;
import use_case.load_pantry.LoadPantryDataAccessInterface;
import use_case.update_stock_level.UpdateStockLevelDataAccessInterface;

/**
 * Persists the user's pantry in a JSON file.
 */
public class FilePantryDataAccessObject
        implements LoadPantryDataAccessInterface, PantryReader,
        UpdateStockLevelDataAccessInterface, AddPantryItemDataAccessInterface {
    private static final String KEY_ITEMS = "items";
    private static final String KEY_NAME = "name";
    private static final String KEY_EXPIRY_DATE = "expiryDate";
    private static final String KEY_STOCK_LEVEL = "stockLevel";

    private final Path filePath;

    /**
     * Creates a DAO that stores the pantry at the given path.
     *
     * @param filePath the JSON file path
     */
    public FilePantryDataAccessObject(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath, "file path cannot be null");
        ensureFileExists();
    }

    /**
     * Creates a DAO that stores the pantry in {@code data/pantry.json}.
     */
    public FilePantryDataAccessObject() {
        this(Path.of("data", "pantry.json"));
    }

    @Override
    public Pantry loadPantry() {
        try {
            final JSONArray itemsJson = readRoot().getJSONArray(KEY_ITEMS);
            final Pantry pantry = new Pantry();
            for (int index = 0; index < itemsJson.length(); index++) {
                pantry.add(fromJson(itemsJson.getJSONObject(index)));
            }
            return pantry;
        }
        catch (JSONException | DateTimeParseException | IllegalArgumentException exception) {
            throw new IllegalStateException("Could not parse pantry file", exception);
        }
    }

    @Override
    public Pantry getPantry() {
        return loadPantry();
    }

    /**
     * Replaces the saved pantry contents.
     *
     * @param pantry the pantry to save
     */
    public void savePantry(Pantry pantry) {
        Objects.requireNonNull(pantry, "pantry cannot be null");
        final JSONArray itemsJson = new JSONArray();
        for (PantryItem item : pantry.sortedByExpiry()) {
            itemsJson.put(toJson(item));
        }
        final JSONObject root = new JSONObject();
        root.put(KEY_ITEMS, itemsJson);
        writeRoot(root);
    }

    private void ensureFileExists() {
        try {
            final Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(filePath)) {
                final JSONObject root = new JSONObject();
                root.put(KEY_ITEMS, new JSONArray());
                writeRoot(root);
            }
        }
        catch (IOException exception) {
            throw new IllegalStateException("Could not create pantry file", exception);
        }
    }

    private JSONObject readRoot() {
        try {
            final String content = Files.readString(filePath, StandardCharsets.UTF_8);
            if (content.isBlank()) {
                final JSONObject emptyRoot = new JSONObject();
                emptyRoot.put(KEY_ITEMS, new JSONArray());
                return emptyRoot;
            }

            final JSONObject root = new JSONObject(content);
            if (!root.has(KEY_ITEMS)) {
                root.put(KEY_ITEMS, new JSONArray());
            }
            return root;
        }
        catch (IOException | JSONException exception) {
            throw new IllegalStateException("Could not read pantry file", exception);
        }
    }

    private void writeRoot(JSONObject root) {
        try {
            Files.writeString(filePath, root.toString(2), StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            throw new IllegalStateException("Could not write pantry file", exception);
        }
    }

    private PantryItem fromJson(JSONObject itemJson) {
        final IngredientName name = IngredientName.fromRawName(itemJson.getString(KEY_NAME));
        final LocalDate expiryDate;
        if (itemJson.isNull(KEY_EXPIRY_DATE)) {
            expiryDate = null;
        }
        else {
            expiryDate = LocalDate.parse(itemJson.getString(KEY_EXPIRY_DATE));
        }
        final StockLevel stockLevel = StockLevel.valueOf(
                itemJson.getString(KEY_STOCK_LEVEL));
        return new PantryItem(name, expiryDate, stockLevel);
    }

    private JSONObject toJson(PantryItem item) {
        final JSONObject itemJson = new JSONObject();
        itemJson.put(KEY_NAME, item.getName().getDisplayName());
        itemJson.put(KEY_EXPIRY_DATE,
                item.getExpiryDate() == null ? JSONObject.NULL : item.getExpiryDate().toString());
        itemJson.put(KEY_STOCK_LEVEL, item.getStockLevel().name());
        return itemJson;
    }
}
