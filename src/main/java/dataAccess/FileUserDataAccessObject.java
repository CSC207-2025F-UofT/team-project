package dataAccess;

import entity.User;
import entity.UserFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.user_login.UserLoginUserDataAccessInterface;
import use_case.user_logout.UserLogoutUserDataAccessInterface;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON-based implementation of the User Data Access Object.
 */
public class FileUserDataAccessObject implements
        UserLoginUserDataAccessInterface,
        UserLogoutUserDataAccessInterface,
        SignupUserDataAccessInterface {

    private final String jsonPath;
    private final UserFactory userFactory;
    private final Map<String, User> accounts = new HashMap<>();

    private String currentUsername;

    public FileUserDataAccessObject(String jsonPath, UserFactory userFactory) {
        this.jsonPath = jsonPath;
        this.userFactory = userFactory;

        File f = new File(jsonPath);

        if (!f.exists()) {
            saveToDisk();  // create empty file
        } else {
            loadFromDisk();
        }
    }

    /**
     * Load all users from JSON file
     */
    private void loadFromDisk() {
        try {
            String text = new String(Files.readAllBytes(Paths.get(jsonPath)));

            JSONArray arr = new JSONArray(text);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                User user = new User(
                        o.getInt("id"),
                        o.getString("name"),
                        o.optString("address", ""),
                        User.idType.valueOf(o.getString("idType")),
                        o.optInt("phoneNumber", 0),
                        o.optString("email", ""),
                        o.getString("username"),
                        o.getString("password"),
                        o.getString("userType")
                );

                accounts.put(user.getUsername(), user);
            }

            System.out.println("Loaded users: " + accounts.size());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON users", e);
        }
    }

    /**
     * Writes all users to disk
     */
    private void saveToDisk() {
        try (FileWriter writer = new FileWriter(jsonPath)) {

            JSONArray arr = new JSONArray();

            for (User user : accounts.values()) {
                arr.put(user.toJson()); // use your User.toJson()
            }

            writer.write(arr.toString(4)); // pretty print

        } catch (Exception e) {
            throw new RuntimeException("Failed to save JSON users", e);
        }
    }

    // ---------------- Interface Methods ----------------

    @Override
    public boolean existsByName(String username) {
        return accounts.containsKey(username);
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void save(User user) {
        // auto-generate ID if missing or 0
        if (user.getId() == 0) {
            int maxId = accounts.values().stream()
                    .mapToInt(User::getId)
                    .max()
                    .orElse(0);
            int newId = maxId + 1;

            user = new User(
                    newId,
                    user.getName(),
                    user.getAddress(),
                    user.getIdType(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getUserType()
            );
        }

        accounts.put(user.getUsername(), user);
        saveToDisk();
    }

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    public void updateUser(User user) {
        accounts.put(user.getUsername(), user);
        save(user);
    }

	public User getCurrentUser() {
		return accounts.get(currentUsername);
	}
}
