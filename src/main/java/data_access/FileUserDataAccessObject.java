package data_access;

import entity.User;
import entity.UserFactory;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DataAccessObject for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface{

    private static final String HEADER = "username,password";
    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();
    private final UserFactory userFactory;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory){
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

                if (!HEADER.equals(header)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;

                while ((row = reader.readLine()) != null) {
                    if(row.isEmpty()){
                        continue;
                    }
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final User user = this.userFactory.create(username, password);
                    accounts.put(username, user);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))){
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                final String line = String.format("%s,%s",
                        user.getName(), user.getPassword());
                writer.write(line);
                writer.newLine();
            }
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
    public boolean existsByName(String name){
        return accounts.containsKey(name);
    }
}
