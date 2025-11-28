package data_access;

import entity.user.User;
import entity.user.UserFactory;
import use_case.changePassword.ChangePasswordUserDataAccessInterface;
import use_case.createAccount.CreateAccountUserDataAccessInterface;
import use_case.signIn.SignInUserDataAccessInterface;

import java.io.*;
import java.util.*;

public class FileUserDataAccessObjectWithLocations implements CreateAccountUserDataAccessInterface,
        SignInUserDataAccessInterface, UserDataAccessInterface, ChangePasswordUserDataAccessInterface {

    // CSV header
    private static final String HEADER = "username,password,locations";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    private final UserFactory userFactory;

    private String currentUsername = null;

    public FileUserDataAccessObjectWithLocations(String csvPath, UserFactory userFactory) {
        this.userFactory = userFactory;
        this.csvFile = new File(csvPath);

        // order：username, password, locations, token
        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("locations", 2);

        // if file is empty, just write header
        if (csvFile.length() == 0) {
            save();
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!HEADER.equals(header)) {
                    throw new RuntimeException(String.format(
                            "header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",", -1);

                    final String username = col[headers.get("username")];
                    final String password = col[headers.get("password")];

                    // locations
                    final String locationsStr = col[headers.get("locations")];
                    List<String> locations;
                    if (locationsStr == null || locationsStr.isEmpty()) {
                        locations = new ArrayList<>();
                    } else {
                        locations = new ArrayList<>(Arrays.asList(locationsStr.split(";")));
                    }


                    final User user = userFactory.create(username, password, locations);
                    accounts.put(username, user);
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Write all users back to CSV (header + data).
     */
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {

            // header: username,password,locations,token
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                String locationsField = String.join(";", user.getLocations());

                final String line = String.format("%s,%s,%s",
                        user.getName(),
                        user.getPassword(),
                        locationsField);

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException ex) {
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
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void addLocation(String location) {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is signed in.");
        }

        User user = accounts.get(currentUsername);
        if (user == null) {
            throw new RuntimeException("Current user not found.");
        }
        user.addLocation(location);

        // save back to map + CSV
        accounts.put(currentUsername, user);
        save();
    }

    @Override
    public List<String> getLocations() {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is signed in.");
        }

        User user = accounts.get(currentUsername);
        if (user == null) {
            throw new RuntimeException("Current user not found.");
        }

        // Return a copy to protect domain state
        return new ArrayList<>(user.getLocations());
    }

    @Override
    public void changePassword(User user) {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is signed in.");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        final User existing = accounts.get(currentUsername);
        if (existing == null) {
            throw new RuntimeException("Current user not found.");
        }

        // Keep existing locations; only change password
        final List<String> locationsCopy = new ArrayList<>(existing.getLocations());
        final User updated = userFactory.create(currentUsername, user.getPassword(), locationsCopy);

        accounts.put(currentUsername, updated);
        save();

    }
}
