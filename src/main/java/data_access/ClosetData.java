package data_access;

import entity.Category;
import entity.ClothingItem;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.add_clothing.AddClothingDataAccessInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Saves and loads every user's closet to a JSON file (data/closet.json),
 * grouped by username:
 * {"ginni": [ {"id": "c001", "name": "Pink Dress Shirt", ...}, ... ]}
 * Loads the whole file when created and rewrites it after every change.
 * Also remembers who is logged in, so screens know whose closet to use.
 */
public class ClosetData implements AddClothingDataAccessInterface {
    private static final String DEFAULT_FILE = "data/closet.json";
    private static final String GUEST = "guest";

    private final Path jsonFile;
    private final Map<String, List<ClothingItem>> closets = new HashMap<>();
    private String currentUsername = GUEST;

    /** Uses data/closet.json. */
    public ClosetData() {
        this(DEFAULT_FILE);
    }

    /**
     * Uses the given JSON file (tests use a temporary file).
     *
     * @param jsonPath where the closet file lives
     */
    public ClosetData(String jsonPath) {
        this.jsonFile = Paths.get(jsonPath);
        load();
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Called after a successful login so later saves go to the right closet.
     *
     * @param username the user who just logged in
     */
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }


    @Override
    public String generateItemId(String username) {
        int highest = 0;
        for (ClothingItem item : closetOf(username)) {
            final String digits = item.getId().replaceAll("\\D", "");
            if (!digits.isEmpty()) {
                highest = Math.max(highest, Integer.parseInt(digits));
            }
        }
        return String.format("c%03d", highest + 1);
    }

    @Override
    public void saveItem(String username, ClothingItem item) {
        closets.computeIfAbsent(username, key -> new ArrayList<>()).add(item);
        save();
    }


    /**
     * Every item in one user's closet.
     *
     * @param username whose closet
     * @return a copy of their items (empty if they have none)
     */
    public List<ClothingItem> getAllItems(String username) {
        return new ArrayList<>(closetOf(username));
    }

    /**
     * Finds one item by its id.
     *
     * @param username whose closet
     * @param itemId e.g. "c001"
     * @return the item, or null if there's no such id
     */
    public ClothingItem getItemById(String username, String itemId) {
        for (ClothingItem item : closetOf(username)) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }


    private List<ClothingItem> closetOf(String username) {
        return closets.getOrDefault(username, new ArrayList<>());
    }

    private void load() {
        if (!Files.exists(jsonFile)) {
            return;
        }
        try {
            final String text = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
            final JSONObject root = new JSONObject(text);
            for (String username : root.keySet()) {
                final JSONArray array = root.getJSONArray(username);
                final List<ClothingItem> items = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    items.add(fromJson(array.getJSONObject(i)));
                }
                closets.put(username, items);
            }
        }
        catch (IOException ex) {
            throw new IllegalStateException("Couldn't read " + jsonFile, ex);
        }
    }

    private void save() {
        final JSONObject root = new JSONObject();
        for (Map.Entry<String, List<ClothingItem>> entry : closets.entrySet()) {
            final JSONArray array = new JSONArray();
            for (ClothingItem item : entry.getValue()) {
                array.put(toJson(item));
            }
            root.put(entry.getKey(), array);
        }
        try {
            if (jsonFile.getParent() != null) {
                Files.createDirectories(jsonFile.getParent());
            }
            Files.write(jsonFile, root.toString(2).getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException ex) {
            throw new IllegalStateException("Couldn't write " + jsonFile, ex);
        }
    }

    private static JSONObject toJson(ClothingItem item) {
        final JSONObject json = new JSONObject();
        json.put("id", item.getId());
        json.put("name", item.getName());
        json.put("category", item.getCategory().name());
        json.put("subCategory", item.getSubCategory());
        json.put("color", item.getColor());
        json.put("style", item.getStyle());
        json.put("layer", item.isLayer());
        json.put("warmthLevels", new JSONArray(item.getWarmthLevels()));
        json.put("imagePath", item.getImagePath());
        return json;
    }

    private static ClothingItem fromJson(JSONObject json) {
        return new ClothingItem(
                json.getString("id"),
                json.getString("name"),
                Category.valueOf(json.getString("category")),
                json.getString("subCategory"),
                json.getString("color"),
                json.getString("style"),
                json.getBoolean("layer"),
                readWarmthLevels(json.getJSONArray("warmthLevels")),
                json.getString("imagePath"));
    }

    private static List<Integer> readWarmthLevels(JSONArray array) {
        final List<Integer> range = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            range.add(array.getInt(i));
        }
        return range;
    }
}
