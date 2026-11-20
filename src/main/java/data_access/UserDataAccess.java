package data_access;

import entity.Document;
import entity.User;

import java.io.*;
import java.util.*;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class UserDataAccess {

    private static final String HEADER = "name,documentsCreated,documentsAccessed";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public UserDataAccess(String csvPath) {
        csvFile = new File(csvPath);

        headers.put("name", 0);
        headers.put("documentsCreated", 1);
        headers.put("documentsAccessed", 2);
    }
    public void load() {
        if (csvFile.length() == 0) {
            save();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            final String header = reader.readLine();

            if (!header.equals(HEADER)) {
                throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
            }

            String row;
            while ((row = reader.readLine()) != null) {
                final String[] col = row.split(",", 4);
                final String name = String.valueOf(col[headers.get("name")]);
                final User user = new User(name, new ArrayList<>(), new ArrayList<>());
                accounts.put(name, user);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                StringJoiner documentsCreated = new StringJoiner(";");
                for (Document documentCreated : user.getDocumentsCreated()) {
                    documentsCreated.add(documentCreated.getName());
                }
                StringJoiner documentsAccessed = new StringJoiner(";");
                for (Document documentAccessed : user.getDocumentsAccessed()) {
                    documentsAccessed.add(documentAccessed.getName());
                }
                final String line = String.format("%s,%s,%s",
                        user.getName(), documentsCreated, documentsAccessed);
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void save(User user) {
        accounts.put(user.getName(), user);
        save();
    }

    public User get(String name) { return accounts.get(name); }

    public boolean existsByName(String name) { return accounts.containsKey(name); }

}
