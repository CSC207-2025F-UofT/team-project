package data_access;

import entity.User;
import entity.AdventureGame;
import entity.GameMap;
import entity.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileDataAccess {
    private static final String FILE_PATH = "userdata.json";

    public FileDataAccess() {
    }

    // Save ANY object to JSON (more flexible)
    public <T> void save(T data) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            if (data instanceof AdventureGame) {
                serializeAdventureGame((AdventureGame) data).write(writer, 4, 0);
            } else {
                // Fallback for other objects
                new JSONObject(data).write(writer, 4, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load data of specified type
    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> clazz) {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject json = new JSONObject(tokener);

            if (clazz.equals(AdventureGame.class)) {
                return (T) deserializeAdventureGame(json);
            }

            return null;
        } catch (IOException | JSONException e) {
            // File doesn't exist, is empty, or contains invalid JSON
            return null;
        }
    }

    // --- Serialization Logic ---

    private JSONObject serializeAdventureGame(AdventureGame game) {
        JSONObject json = new JSONObject();
        // Wrap user in JSONObject (assumes User follows Java Bean conventions)
        json.put("user", new JSONObject(game.getUser()));
        json.put("gameMap", serializeGameMap(game.getGameMap()));

        // Store pathHistory as indices instead of full Location objects
        // This ensures we maintain object identity when deserializing
        JSONArray pathIndicesArray = new JSONArray();
        List<Location> mapLocations = getGameMapLocations(game.getGameMap());

        for (Location pathLoc : game.getPathHistory()) {
            int index = mapLocations.indexOf(pathLoc);
            if (index != -1) {
                pathIndicesArray.put(index);
            } else {
                // Fallback: if location not found in map, store -1
                // This shouldn't happen in normal gameplay
                pathIndicesArray.put(-1);
            }
        }
        json.put("pathHistory", pathIndicesArray);

        return json;
    }

    private JSONObject serializeGameMap(GameMap map) {
        JSONObject json = new JSONObject();
        json.put("currentLocationIndex", map.getCurrentLocationIndex());
        
        JSONArray locationsArray = new JSONArray();
        // We need to access locations. Since GameMap doesn't expose list directly, 
        // we might need to reconstruct or add a getter.
        // Assuming we can iterate via map size or access internal list if we add a getter.
        // For now, let's assume we added getLocations() or similar, OR use reflection if needed.
        // BUT cleaner is to rely on public API. 
        // Let's check if GameMap allows access. 
        // Actually, GameMap usually holds the list. 
        // If no getter, we might need to add one to GameMap or use reflection.
        // However, standard GameMap usually doesn't expose the list. 
        // Let's use reflection here to avoid modifying entity just for DAO if strict, 
        // OR better, add `getLocations()` to GameMap.
        // Since I cannot modify other files easily here without separate blocks, 
        // I will use reflection for the private field 'locations' to respect existing constraints.
        
        try {
            Field locationsField = GameMap.class.getDeclaredField("locations");
            locationsField.setAccessible(true);
            List<Location> locations = (List<Location>) locationsField.get(map);
            
            for (Location loc : locations) {
                locationsArray.put(serializeLocation(loc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("locations", locationsArray);
        
        return json;
    }

    private JSONObject serializeLocation(Location location) {
        JSONObject json = new JSONObject();
        json.put("name", location.getName());
        json.put("latitude", location.getLatitude());
        json.put("longitude", location.getLongitude());
        return json;
    }

    // --- Deserialization Logic ---

    private AdventureGame deserializeAdventureGame(JSONObject json) {
        // Deserialize User using reflection since we can't modify the User class easily in this context
        // or reliance on standard setters
        JSONObject userJson = json.getJSONObject("user");
        User user = new User();

        for (String key : userJson.keySet()) {
            try {
                Field field = User.class.getDeclaredField(key);
                field.setAccessible(true);
                Object value = userJson.get(key);
                // Basic type conversion if needed
                if (value instanceof Integer && (field.getType() == double.class || field.getType() == Double.class)) {
                    field.set(user, ((Integer) value).doubleValue());
                } else {
                    field.set(user, value);
                }
            } catch (Exception e) {
                // Skip fields that can't be set
            }
        }

        GameMap map = deserializeGameMap(json.getJSONObject("gameMap"));
        List<Location> mapLocations = getGameMapLocations(map);

        List<Location> path = new LinkedList<>();
        if (json.has("pathHistory")) {
            JSONArray pathArray = json.getJSONArray("pathHistory");

            // Check if pathHistory contains indices (new format) or objects (old format)
            if (pathArray.length() > 0) {
                Object firstElement = pathArray.get(0);

                if (firstElement instanceof Integer) {
                    // format: array of indices
                    for (int i = 0; i < pathArray.length(); i++) {
                        int index = pathArray.getInt(i);
                        if (index >= 0 && index < mapLocations.size()) {
                            // Add reference to the SAME Location object from GameMap
                            path.add(mapLocations.get(index));
                        }
                    }
                }
            }
        }

        if (path.isEmpty()) {
            path.add(map.getCurrentLocation());
        }

        return new AdventureGame(user, map, path);
    }

    private GameMap deserializeGameMap(JSONObject json) {
        int currentIndex = json.getInt("currentLocationIndex");
        JSONArray locationsArray = json.getJSONArray("locations");
        
        List<Location> loadedLocations = new ArrayList<>();
        for (int i = 0; i < locationsArray.length(); i++) {
            loadedLocations.add(deserializeLocation(locationsArray.getJSONObject(i)));
        }
        
        return new GameMap(loadedLocations, currentIndex);
    }

    private Location deserializeLocation(JSONObject json) {
        String name = json.getString("name");
        double latitude = json.getDouble("latitude");
        double longitude = json.getDouble("longitude");
        // Monster is currently initialized to null as it's not persisted
        return new Location(name, latitude, longitude, null);
    }

    // Helper method to get locations list from GameMap using reflection
    private List<Location> getGameMapLocations(GameMap gameMap) {
        try {
            Field locationsField = GameMap.class.getDeclaredField("locations");
            locationsField.setAccessible(true);
            return (List<Location>) locationsField.get(gameMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}