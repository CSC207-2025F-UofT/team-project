package data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.AdopterPreferences;
import entity.Species;
import entity.User;
import entity.User.Role;
import entity.UserFactory;
import use_case.admin_signup.AdminSignupUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.review_applications.ReviewApplicationsUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * DAO for user data implemented using a JSON file to persist the data.
 * Users are keyed by id; email is also unique.
 * The currently logged-in user is kept in memory only.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
                                                 AdminSignupUserDataAccessInterface,
                                                 LoginUserDataAccessInterface,
                                                 ReviewApplicationsUserDataAccessInterface {

    private static final String PREFERENCE = "preference";
    private static final String PREFERRED_SPECIES = "preferredSpecies";

    private final Path jsonPath;
    private final UserFactory userFactory;
    private Map<String, User> usersById = new LinkedHashMap<>();
    private Map<String, String> idByEmail = new HashMap<>();

    private String currentUserId;

    /**
     * Opens the user store, or starts with no users if the file is absent.
     * @param jsonPath the user data file
     * @param userFactory factory for creating user objects
     * @throws IllegalStateException if the file exists but cannot be read or has an unexpected format
     */
    public FileUserDataAccessObject(Path jsonPath, UserFactory userFactory) {
        this.jsonPath = jsonPath.toAbsolutePath();
        this.userFactory = userFactory;
        load();
    }

    // ---------- Reading ----------

    private void load() {
        try {
            final JSONArray array = new JSONArray(Files.readString(jsonPath));
            for (int i = 0; i < array.length(); i++) {
                final User user = readUser(array.getJSONObject(i));
                if (usersById.containsKey(user.getId())) {
                    throw new JSONException("Duplicate user ID: " + user.getId());
                }
                if (idByEmail.containsKey(user.getEmail())) {
                    throw new JSONException("Duplicate user email: " + user.getEmail());
                }
                usersById.put(user.getId(), user);
                idByEmail.put(user.getEmail(), user.getId());
            }
        }
        catch (NoSuchFileException ex) {
            // First run: no file yet, so there are no users.
        }
        catch (IOException | JSONException | IllegalArgumentException ex) {
            throw new IllegalStateException("Could not load users from " + jsonPath, ex);
        }
    }

    private User readUser(JSONObject json) {
        final AdopterPreferences preference;
        if (json.isNull(PREFERENCE)) {
            preference = null;
        }
        else {
            preference = readPreference(json.getJSONObject(PREFERENCE));
        }
        return userFactory.create(json.getString("username"), json.getString("id"),
                json.getString("email"), json.getString("passwordHash"),
                Role.valueOf(json.getString("role")), preference);
    }

    private static AdopterPreferences readPreference(JSONObject json) {
        final Species species;
        if (json.isNull(PREFERRED_SPECIES)) {
            species = null;
        }
        else {
            species = Species.valueOf(json.getString(PREFERRED_SPECIES));
        }
        return new AdopterPreferences(
                json.getBoolean("hasChildren"),
                json.getBoolean("hasOtherPets"),
                json.getBoolean("canHandleMedicalNeeds"),
                json.getBoolean("canHandleKitten"),
                json.getBoolean("canHandlePregnantAnimal"),
                species);
    }

    // ---------- Writing ----------

    private static JSONObject writeUser(User user) {
        final JSONObject json = new JSONObject();
        json.put("username", user.getName());
        json.put("id", user.getId());
        json.put("email", user.getEmail());
        json.put("passwordHash", user.getPasswordHash());
        json.put("role", user.getRole().name());
        if (user.getPreference() == null) {
            json.put(PREFERENCE, JSONObject.NULL);
        }
        else {
            json.put(PREFERENCE, writePreference(user.getPreference()));
        }
        return json;
    }

    private static JSONObject writePreference(AdopterPreferences preference) {
        final JSONObject json = new JSONObject();
        json.put("hasChildren", preference.hasChildren());
        json.put("hasOtherPets", preference.hasOtherPets());
        json.put("canHandleMedicalNeeds", preference.canHandleMedicalNeeds());
        json.put("canHandleKitten", preference.canHandleKitten());
        json.put("canHandlePregnantAnimal", preference.canHandlePregnantAnimal());
        if (preference.getPreferredSpecies() == null) {
            json.put(PREFERRED_SPECIES, JSONObject.NULL);
        }
        else {
            json.put(PREFERRED_SPECIES, preference.getPreferredSpecies().name());
        }
        return json;
    }

    /**
     * Writes the given users to the file, replacing it only once the full snapshot is written.
     */
    private void writeFile(Map<String, User> users) {
        final JSONArray array = new JSONArray();
        for (User user : users.values()) {
            array.put(writeUser(user));
        }

        Path temporaryPath = null;
        try {
            temporaryPath = Files.createTempFile(jsonPath.getParent(), "users-", ".tmp");
            Files.writeString(temporaryPath, array.toString(2));
            Files.move(temporaryPath, jsonPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ex) {
            if (temporaryPath != null) {
                try {
                    Files.deleteIfExists(temporaryPath);
                }
                catch (IOException cleanupFailure) {
                    ex.addSuppressed(cleanupFailure);
                }
            }
            throw new IllegalStateException("Could not save users to " + jsonPath, ex);
        }
    }

    // ---------- Signup / Admin signup ----------

    @Override
    public synchronized boolean existsById(String id) {
        return usersById.containsKey(id);
    }

    @Override
    public synchronized boolean existsByEmail(String email) {
        return idByEmail.containsKey(email);
    }

    @Override
    public synchronized void save(User user) {
        final Map<String, User> updatedUsers = new LinkedHashMap<>(usersById);
        final Map<String, String> updatedEmails = new HashMap<>(idByEmail);
        final User previous = updatedUsers.put(user.getId(), user);
        if (previous != null) {
            updatedEmails.remove(previous.getEmail());
        }
        updatedEmails.put(user.getEmail(), user.getId());

        // Only update memory after the file has been written successfully.
        writeFile(updatedUsers);
        usersById = updatedUsers;
        idByEmail = updatedEmails;
    }

    // ---------- Login ----------

    @Override
    public synchronized User getByEmail(String email) {
        final String id = idByEmail.get(email);
        return id == null ? null : usersById.get(id);
    }

    @Override
    public synchronized String getUserName(String userId) {
        final User user = usersById.get(userId);
        if (user == null) {
            return null;
        }
        return user.getName();
    }

    @Override
    public synchronized void setCurrentUserId(String id) {
        currentUserId = id;
    }
}
