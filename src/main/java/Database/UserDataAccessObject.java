package Database;

import DataAccess.UserDataAccessInterface;
import Entities.User;
import Entities.UserProfile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class UserDataAccessObject implements UserDataAccessInterface {
    @Override
    public void create(User user) throws SQLException {
        UserProfile profile = validateUser(user);

        String sql = "INSERT INTO public.users "
                + "(username, password, first_name, last_name, age, gender, occupation, biography) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, profile.getFirstName());
            statement.setString(4, profile.getLastName());
            statement.setInt(5, profile.getAge());
            statement.setString(6, profile.getGender());
            statement.setString(7, profile.getOccupation());
            statement.setString(8, profile.getBiography());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("Creating the user did not insert exactly one row.");
            }
        }
    }

    @Override
    public void update(String username, User user) throws SQLException {
        UserProfile profile = validateUser(user);
        validateUsername(username);
        if (!username.equals(user.getUsername())) {
            throw new IllegalArgumentException("Username cannot be changed.");
        }

        String sql = "UPDATE public.users SET password = ?, first_name = ?, last_name = ?, age = ?, "
                + "gender = ?, occupation = ?, biography = ? WHERE username = ?";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setString(2, profile.getFirstName());
            statement.setString(3, profile.getLastName());
            statement.setInt(4, profile.getAge());
            statement.setString(5, profile.getGender());
            statement.setString(6, profile.getOccupation());
            statement.setString(7, profile.getBiography());
            statement.setString(8, username);
            if (statement.executeUpdate() != 1) {
                throw new SQLException("Updating the user did not update exactly one row.");
            }
        }
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        validateUsername(username);
        String sql = "SELECT username, password, first_name, last_name, age, gender, occupation, biography "
                + "FROM public.users WHERE username = ?";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapUser(results));
            }
        }
    }

    @Override
    public boolean doesUserExist(String username) throws SQLException {
        validateUsername(username);
        String sql = "SELECT username"
                + "FROM public.users WHERE username = ?";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        }
    }

    private Connection openConnection() throws SQLException {
    return DriverManager.getConnection(
            "jdbc:postgresql://ep-lingering-voice-be3x9s30-pooler.c-9.us-east-2.aws.neon.tech/neondb?sslmode=require",
            "neondb_owner",
            "npg_lQMNDIZV2w5T");
}
    private static User mapUser(ResultSet result) throws SQLException {
        Integer age = result.getObject("age", Integer.class);
        if (age == null) {
            throw new SQLException("A user row has no age, but public.users.age is required.");
        }
        UserProfile profile = UserProfile.UserProfileFactory(
                result.getString("first_name"),
                result.getString("last_name"),
                age,
                result.getString("gender"),
                result.getString("occupation"),
                result.getString("biography"),
                "",
                "");
        return User.UserFactory(result.getString("username"), result.getString("password"), profile);
    }

    private static UserProfile validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is required.");
        }
        validateUsername(user.getUsername());
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password is required.");
        }
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new IllegalArgumentException("User profile with age is required.");
        }
        try {
            profile.getAge();
        } catch (NullPointerException exception) {
            throw new IllegalArgumentException("User profile age is required.", exception);
        }
        return profile;
    }

    private static void validateUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username is required.");
        }
    }
}

