package data_access;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import entity.User;
import entity.User.Role;
import entity.UserFactory;
import use_case.admin_signup.AdminSignupUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * DAO for user data implemented using a CSV file to persist the data.
 * Users are keyed by id; email is also unique.
 * The currently logged-in user is kept in memory only.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
                                                 AdminSignupUserDataAccessInterface,
                                                 LoginUserDataAccessInterface {

    private static final String[] COLUMNS = {"username", "id", "email", "passwordHash", "role", "preference"};
    private static final String HEADER = String.join(",", COLUMNS);

    private final File csvFile;
    private final UserFactory userFactory;
    private final Map<String, User> usersById = new LinkedHashMap<>();
    private final Map<String, String> idByEmail = new HashMap<>();

    private String currentUserId;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file,
     *                          or if the file has an unexpected format
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {
        this.csvFile = new File(csvPath);
        this.userFactory = userFactory;

        if (!csvFile.exists() || csvFile.length() == 0) {
            writeFile();
        }
        else {
            readFile();
        }
    }

    private void readFile() {
        final String content;
        try {
            content = Files.readString(csvFile.toPath());
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        final List<List<String>> rows = parseCsv(content);
        final String header = rows.isEmpty() ? "" : String.join(",", rows.get(0));
        if (!HEADER.equals(header)) {
            throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
        }

        for (int i = 1; i < rows.size(); i++) {
            final List<String> col = rows.get(i);
            if (col.size() == 1 && col.get(0).isEmpty()) {
                // skip blank lines
                continue;
            }
            if (col.size() != COLUMNS.length) {
                throw new RuntimeException("Malformed row " + i + " in " + csvFile + ": " + col);
            }
            final String preference = col.get(5).isEmpty() ? null : col.get(5);
            put(userFactory.create(col.get(0), col.get(1), col.get(2), col.get(3),
                                   Role.valueOf(col.get(4)), preference));
        }
    }

    private void writeFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(csvFile.toPath())) {
            writer.write(HEADER);
            writer.newLine();

            for (User user : usersById.values()) {
                writer.write(String.join(",",
                        escape(user.getName()),
                        escape(user.getId()),
                        escape(user.getEmail()),
                        escape(user.getPasswordHash()),
                        escape(user.getRole().name()),
                        escape(user.getPreference())));
                writer.newLine();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void put(User user) {
        final User previous = usersById.put(user.getId(), user);
        if (previous != null) {
            idByEmail.remove(previous.getEmail());
        }
        idByEmail.put(user.getEmail(), user.getId());
    }

    // ---------- CSV helpers ----------

    /**
     * Quotes a field if it contains a comma, quote or line break; null becomes an empty field.
     */
    private static String escape(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    /**
     * Parses CSV text into rows of fields, supporting double-quoted fields
     * (with "" as an escaped quote and line breaks inside quotes).
     */
    private static List<List<String>> parseCsv(String content) {
        final List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        final StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < content.length(); i++) {
            final char c = content.charAt(i);
            if (inQuotes) {
                if (c == '"' && i + 1 < content.length() && content.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                }
                else if (c == '"') {
                    inQuotes = false;
                }
                else {
                    field.append(c);
                }
            }
            else if (c == '"') {
                inQuotes = true;
            }
            else if (c == ',') {
                row.add(field.toString());
                field.setLength(0);
            }
            else if (c == '\n' || c == '\r') {
                if (c == '\r' && i + 1 < content.length() && content.charAt(i + 1) == '\n') {
                    i++;
                }
                row.add(field.toString());
                field.setLength(0);
                rows.add(row);
                row = new ArrayList<>();
            }
            else {
                field.append(c);
            }
        }
        if (field.length() > 0 || !row.isEmpty()) {
            row.add(field.toString());
            rows.add(row);
        }
        return rows;
    }

    // ---------- Signup / Admin signup ----------

    @Override
    public boolean existsById(String id) {
        return usersById.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return idByEmail.containsKey(email);
    }

    @Override
    public void save(User user) {
        put(user);
        writeFile();
    }

    // ---------- Login ----------

    @Override
    public User getByEmail(String email) {
        final String id = idByEmail.get(email);
        return id == null ? null : usersById.get(id);
    }

    @Override
    public void setCurrentUserId(String id) {
        currentUserId = id;
    }
}
