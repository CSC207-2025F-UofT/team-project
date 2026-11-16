package data_access;

import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/** Stores local accounts with salted password hashes. */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface, ChangePasswordUserDataAccessInterface, LogoutUserDataAccessInterface {

    private static final String HEADER = "username_base64,password_pbkdf2_sha256";
    private static final String LEGACY_HEADER = "username,password";

    private final Path accountFile;
    private final UserFactory userFactory;
    private Map<String, String> accounts = new HashMap<>();
    private String currentUsername;

    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {
        this.accountFile = Path.of(csvPath).toAbsolutePath();
        this.userFactory = userFactory;

        try {
            if (Files.exists(accountFile) && Files.size(accountFile) > 0) {
                load();
            }
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not read local accounts.", exception);
        }
    }

    private void load() throws IOException {
        final boolean legacy;
        try (BufferedReader reader = Files.newBufferedReader(accountFile, Charset.defaultCharset())) {
            String header = reader.readLine();
            legacy = LEGACY_HEADER.equals(header);
            if (!legacy && !HEADER.equals(header)) {
                throw new IOException("Unrecognized account file format.");
            }
            String row;
            while ((row = reader.readLine()) != null) {
                String[] columns = row.split(",", 2);
                if (columns.length != 2 || columns[0].isEmpty() || columns[1].isEmpty()) {
                    throw new IOException("Invalid row in account file.");
                }
                if (legacy) {
                    accounts.put(columns[0], PasswordHash.create(columns[1]));
                }
                else {
                    String username = new String(Base64.getDecoder().decode(columns[0]), StandardCharsets.UTF_8);
                    accounts.put(username, columns[1]);
                }
            }
        }
        if (legacy) {
            write(accounts);
        }
    }

    private void write(Map<String, String> updated) throws IOException {
        Path temporaryFile = Files.createTempFile(accountFile.getParent(), "accounts-", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temporaryFile)) {
                writer.write(HEADER);
                writer.newLine();
                for (Map.Entry<String, String> account : updated.entrySet()) {
                    byte[] username = account.getKey().getBytes(StandardCharsets.UTF_8);
                    writer.write(Base64.getEncoder().encodeToString(username));
                    writer.write(",");
                    writer.write(account.getValue());
                    writer.newLine();
                }
            }
            try {
                Files.move(temporaryFile, accountFile, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            }
            catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, accountFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    @Override
    public void save(User user) {
        Map<String, String> updated = new HashMap<>(accounts);
        updated.put(user.getName(), PasswordHash.create(user.getPassword()));
        try {
            write(updated);
            accounts = updated;
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not save local accounts.", exception);
        }
    }

    @Override
    public User get(String username) {
        String hash = accounts.get(username);
        return hash == null ? null : userFactory.create(username, hash);
    }

    @Override
    public boolean passwordMatches(String username, String password) {
        String hash = accounts.get(username);
        return hash != null && PasswordHash.matches(password, hash);
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
        if (!existsByName(user.getName())) {
            throw new IllegalArgumentException("A local account is required.");
        }
        save(user);
    }
}
