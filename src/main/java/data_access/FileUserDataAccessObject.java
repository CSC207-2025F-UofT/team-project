package data_access;

import entity.User;
import entity.UserFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores users in a JSON file so accounts survive restarts.
 * File format: {"users": [{"username": "...", "password": "..."}, ...]}
 */
public class FileUserDataAccessObject implements UserDataAccessObject {
    private final Path filePath;
    private final UserFactory userFactory;
    private final Map<String, User> users = new LinkedHashMap<>();
    private String currentUsername = null;

    public FileUserDataAccessObject(String filePath, UserFactory userFactory) {
        this.filePath = Path.of(filePath);
        this.userFactory = userFactory;
        if (Files.exists(this.filePath)) {
            load();
        }
    }

    private void load() {
        try {
            JSONObject root = new JSONObject(Files.readString(filePath));
            JSONArray array = root.getJSONArray("users");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                User user = userFactory.create(obj.getString("username"), obj.getString("password"));
                users.put(user.getUsername(), user);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + filePath, e);
        }
    }

    private void writeToFile() {
        JSONArray array = new JSONArray();
        for (User user : users.values()) {
            JSONObject obj = new JSONObject();
            obj.put("username", user.getUsername());
            obj.put("password", user.getPassword());
            array.put(obj);
        }
        JSONObject root = new JSONObject();
        root.put("users", array);
        try {
            Files.writeString(filePath, root.toString(2));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write " + filePath, e);
        }
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
        writeToFile();
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}
