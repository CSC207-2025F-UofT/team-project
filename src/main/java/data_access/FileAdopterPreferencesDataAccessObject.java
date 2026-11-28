package data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import entity.AdopterPreferences;
import use_case.check_compatibility.AdopterPreferencesDataAccessInterface;

/**
 * Keeps each adopter's saved preferences in a JSON file, keyed by adopter id.
 * A missing or unreadable file is treated as "nothing saved yet".
 */
public class FileAdopterPreferencesDataAccessObject implements AdopterPreferencesDataAccessInterface {
    private static final String HAS_CHILDREN = "hasChildren";
    private static final String HAS_OTHER_PETS = "hasOtherPets";
    private static final String MEDICAL = "canHandleMedicalNeeds";
    private static final String KITTEN = "canHandleKitten";
    private static final String PREGNANT = "canHandlePregnantAnimal";

    private final Path jsonPath;
    private final Map<String, AdopterPreferences> saved = new HashMap<>();

    /**
     * Opens the file, reading whatever it holds.
     *
     * @param jsonPath the file to read and write
     */
    public FileAdopterPreferencesDataAccessObject(Path jsonPath) {
        this.jsonPath = jsonPath;
        load();
    }

    @Override
    public synchronized Optional<AdopterPreferences> findByAdopterId(String adopterId) {
        return Optional.ofNullable(saved.get(adopterId));
    }

    @Override
    public synchronized void save(String adopterId, AdopterPreferences preferences) {
        saved.put(adopterId, preferences);
        write();
    }

    private void load() {
        try {
            final JSONObject root = new JSONObject(Files.readString(jsonPath));
            for (String adopterId : root.keySet()) {
                final JSONObject json = root.getJSONObject(adopterId);
                saved.put(adopterId, new AdopterPreferences(json.getBoolean(HAS_CHILDREN),
                        json.getBoolean(HAS_OTHER_PETS), json.getBoolean(MEDICAL), json.getBoolean(KITTEN),
                        json.getBoolean(PREGNANT), null));
            }
        } catch (NoSuchFileException | JSONException error) {
            saved.clear();
        } catch (IOException error) {
            throw new IllegalStateException("Could not read " + jsonPath, error);
        }
    }

    private void write() {
        final JSONObject root = new JSONObject();
        for (Map.Entry<String, AdopterPreferences> entry : saved.entrySet()) {
            final AdopterPreferences preferences = entry.getValue();
            final JSONObject json = new JSONObject();
            json.put(HAS_CHILDREN, preferences.hasChildren());
            json.put(HAS_OTHER_PETS, preferences.hasOtherPets());
            json.put(MEDICAL, preferences.canHandleMedicalNeeds());
            json.put(KITTEN, preferences.canHandleKitten());
            json.put(PREGNANT, preferences.canHandlePregnantAnimal());
            root.put(entry.getKey(), json);
        }
        try {
            Files.writeString(jsonPath, root.toString(2));
        } catch (IOException error) {
            throw new IllegalStateException("Could not write " + jsonPath, error);
        }
    }
}
