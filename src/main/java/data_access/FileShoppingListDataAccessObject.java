package data_access;

import entity.IngredientName;
import entity.ShoppingItem;
import entity.ShoppingList;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.generate_shopping_list.GenerateShoppingListDataAccessInterface;
import use_case.load_shopping_list.LoadShoppingListDataAccessInterface;
import use_case.toggle_bought.ToggleBoughtDataAccessInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Persists shopping lists by week in a JSON file.
 */
public class FileShoppingListDataAccessObject
        implements LoadShoppingListDataAccessInterface,
        GenerateShoppingListDataAccessInterface,
        ToggleBoughtDataAccessInterface {

    private static final String KEY_LISTS = "lists";
    private static final String KEY_WEEK_START = "weekStart";
    private static final String KEY_ITEMS = "items";
    private static final String KEY_NAME = "name";
    private static final String KEY_REASON = "reason";
    private static final String KEY_NEEDED_FOR = "neededFor";
    private static final String KEY_BOUGHT = "bought";

    private final Path filePath;

    /**
     * Creates a DAO that stores lists at the given file path.
     *
     * @param filePath the JSON file path
     */
    public FileShoppingListDataAccessObject(Path filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    /**
     * Creates a DAO that stores lists in {@code data/shopping_list.json}.
     */
    public FileShoppingListDataAccessObject() {
        this(Path.of("data", "shopping_list.json"));
    }

    @Override
    public ShoppingList loadShoppingList(LocalDate weekStart) {
        JSONObject root = readRoot();
        JSONArray lists = root.getJSONArray(KEY_LISTS);
        for (int i = 0; i < lists.length(); i++) {
            JSONObject listJson = lists.getJSONObject(i);
            if (weekStart.toString().equals(listJson.getString(KEY_WEEK_START))) {
                return fromJson(listJson);
            }
        }
        return null;
    }

    @Override
    public void saveShoppingList(ShoppingList shoppingList) {
        JSONObject root = readRoot();
        JSONArray lists = root.getJSONArray(KEY_LISTS);
        String weekKey = shoppingList.getWeekStart().toString();
        int existingIndex = -1;
        for (int i = 0; i < lists.length(); i++) {
            if (weekKey.equals(lists.getJSONObject(i).getString(KEY_WEEK_START))) {
                existingIndex = i;
                break;
            }
        }
        JSONObject listJson = toJson(shoppingList);
        if (existingIndex >= 0) {
            lists.put(existingIndex, listJson);
        }
        else {
            lists.put(listJson);
        }
        writeRoot(root);
    }

    private void ensureFileExists() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(filePath)) {
                JSONObject root = new JSONObject();
                root.put(KEY_LISTS, new JSONArray());
                Files.writeString(filePath, root.toString(2), StandardCharsets.UTF_8);
            }
        }
        catch (IOException exception) {
            throw new IllegalStateException("Could not create shopping list file", exception);
        }
    }

    private JSONObject readRoot() {
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            if (content.isBlank()) {
                JSONObject root = new JSONObject();
                root.put(KEY_LISTS, new JSONArray());
                return root;
            }
            JSONObject root = new JSONObject(content);
            if (!root.has(KEY_LISTS)) {
                root.put(KEY_LISTS, new JSONArray());
            }
            return root;
        }
        catch (IOException exception) {
            throw new IllegalStateException("Could not read shopping list file", exception);
        }
    }

    private void writeRoot(JSONObject root) {
        try {
            Files.writeString(filePath, root.toString(2), StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            throw new IllegalStateException("Could not write shopping list file", exception);
        }
    }

    private ShoppingList fromJson(JSONObject listJson) {
        LocalDate weekStart = LocalDate.parse(listJson.getString(KEY_WEEK_START));
        JSONArray itemsJson = listJson.getJSONArray(KEY_ITEMS);
        List<ShoppingItem> items = new ArrayList<>();
        for (int i = 0; i < itemsJson.length(); i++) {
            JSONObject itemJson = itemsJson.getJSONObject(i);
            Set<String> neededFor = new LinkedHashSet<>();
            JSONArray neededForJson = itemJson.getJSONArray(KEY_NEEDED_FOR);
            for (int j = 0; j < neededForJson.length(); j++) {
                neededFor.add(neededForJson.getString(j));
            }
            ShoppingItem item = new ShoppingItem(
                    IngredientName.fromRawName(itemJson.getString(KEY_NAME)),
                    ShoppingItem.Reason.valueOf(itemJson.getString(KEY_REASON)),
                    neededFor);
            item.setBought(itemJson.getBoolean(KEY_BOUGHT));
            items.add(item);
        }
        return new ShoppingList(weekStart, items);
    }

    private JSONObject toJson(ShoppingList shoppingList) {
        JSONObject listJson = new JSONObject();
        listJson.put(KEY_WEEK_START, shoppingList.getWeekStart().toString());
        JSONArray itemsJson = new JSONArray();
        for (ShoppingItem item : shoppingList.getItems()) {
            JSONObject itemJson = new JSONObject();
            itemJson.put(KEY_NAME, item.getName().getDisplayName());
            itemJson.put(KEY_REASON, item.getReason().name());
            itemJson.put(KEY_BOUGHT, item.isBought());
            JSONArray neededForJson = new JSONArray();
            for (String recipeName : item.getNeededFor()) {
                neededForJson.put(recipeName);
            }
            itemJson.put(KEY_NEEDED_FOR, neededForJson);
            itemsJson.put(itemJson);
        }
        listJson.put(KEY_ITEMS, itemsJson);
        return listJson;
    }
}
