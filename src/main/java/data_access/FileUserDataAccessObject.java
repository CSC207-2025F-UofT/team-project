package data_access;

import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.change_username.ChangeUsernameUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.search_user.SearchUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import java.util.List;
import java.util.stream.Collectors;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
                                                 LoginUserDataAccessInterface,
                                                 ChangePasswordUserDataAccessInterface,
                                                 LogoutUserDataAccessInterface, ChangeUsernameUserDataAccessInterface, SearchUserDataAccessInterface {

    private static final String HEADER = "username,password";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();
    private final UserFactory userFactory;
    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {

        csvFile = new File(csvPath);
        this.userFactory = userFactory;
        headers.put("username", 0);
        headers.put("password", 1);

        if (csvFile.length() == 0) {
            save();
        }
        else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final User user = userFactory.create(username, password);
                    accounts.put(username, user);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                final String line = String.format("%s,%s",
                        user.getName(), user.getPassword());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getName(), user);
        this.save();
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
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getName(), user);
        save();
    }

    @Override
    public void changeUsername(String oldUsername, String newUsername) {
        // Check if the user exists in the 'accounts' map
        if (this.accounts.containsKey(oldUsername)) {

            // Retrieve the old User object
            User oldUser = this.accounts.get(oldUsername);

            // Remove the entry indexed by the old username
            this.accounts.remove(oldUsername);

            // Create a new User entity with the new username, reusing the old password
            User newUser = userFactory.create(newUsername, oldUser.getPassword());

            // Add the new user to the map using the new username as the key
            this.accounts.put(newUsername, newUser);

            // Save the updated map state to the file (assuming you have a save() method)
            this.save();
        }
    }
    @Override
    public List<String> searchUsers(String query) {
        // Case-insensitive containment search
        String lowerCaseQuery = query.toLowerCase();

        return this.accounts.keySet().stream()
                .filter(username -> username.toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    public Map<String, entity.User> getAccounts() {
        return accounts;
    }
}
