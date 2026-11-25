
package data_access;

import entity.User;
import user_case.Login.LoginUserDataAccessInterface;
import user_case.Logout.LogoutUserDataAccessInterface;
import user_case.SignUp.SignupUserDataAccessInterface;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class UserDataAccess implements LoginUserDataAccessInterface, SignupUserDataAccessInterface, LogoutUserDataAccessInterface {

    private final Properties users = new Properties();

    private final Path file = Path.of(
            System.getProperty("user.home"),
            ".flicksy", "users.properties");

    private String currentUsername;

    // Load existing accounts
    public UserDataAccess() {
        try {
            Files.createDirectories(file.getParent());

            if (Files.exists(file)) {
                try (InputStream input = Files.newInputStream(file)) {
                    users.load(input);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot load users", e);
        }
    }

    @Override
    public boolean existByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        String username = user.getName();

        if (existByName(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String password = BCrypt.hashpw(
                user.getPassword(), BCrypt.gensalt());

        users.setProperty(username, password);

        try (OutputStream output = Files.newOutputStream(file)) {
            users.store(output, "Flicksy Users");
        }
        catch (IOException e) {
            users.remove(username);
            throw new RuntimeException("Cannot save user", e);
        }
    }
    @Override
    public User get(String username) {
        String passwordHash = users.getProperty(username);

        if (passwordHash == null) {
            return null;
        }

        return new User(username, passwordHash);
    }
    @Override
    public boolean checkPassword(String username, String password) {
        String savedPassword = users.getProperty(username);

        return savedPassword != null
                && BCrypt.checkpw(password, savedPassword);
    }
    @Override
    public void setCurrentUsername(String username) {
        currentUsername = username;
    }
    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }
    @Override
    public String getUsername() {
        return currentUsername;
    }
}
