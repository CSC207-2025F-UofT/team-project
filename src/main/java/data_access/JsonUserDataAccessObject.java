package data_access;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;  // IMPORT THIS
import entity.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a JSON file to persist the data.
 * More flexible than CsvUserDataAccessObject since JSON is able to store Object data.
 */
public class JsonUserDataAccessObject implements UserDataAccessInterface {
    private JsonNode root;

    private final ObjectMapper mapper = new ObjectMapper();
    private final File file;
    private final Map<String, User> accounts = new HashMap<>();

    private String currentUsername;

    public JsonUserDataAccessObject(String jsonFilePath) {
        // register java.time to Jackson ObjectMapper
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        this.file = new File(jsonFilePath);
        try {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }

            // Try to read and parse
            if (file.exists() && file.length() > 0) {
                try {
                    this.root = mapper.readTree(file);

                    if (root.isArray()) {
                        for (JsonNode node : root) {
                            User user = mapper.treeToValue(node, User.class);
                            accounts.put(user.getUsername(), user);
                        }
                    }
                    else {
                        // Not an array - reset
                        resetFile();
                    }
                }
                catch (Exception e) {
                    // Corrupted - reset
                    System.err.println("Corrupted JSON detected, resetting file");
                    resetFile();
                }
            }
            else {
                // Empty or missing - initialize
                resetFile();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to initialize JSON file", e);
        }
    }

    private void resetFile() throws IOException {
        mapper.writeValue(file, new Object[0]);
        this.root = mapper.readTree(file);
    }

    @Override
    public boolean existsByName(String username) {
        return accounts.containsKey(username);
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void save(User user) {
        accounts.put(user.getUsername(), user);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, accounts.values());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user data", e);
        }
    }
}