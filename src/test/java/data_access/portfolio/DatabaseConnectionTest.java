package data_access.portfolio;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseConnectionTest {
    private static final Path DATABASE_FILE = Path.of("portfolio.db");

    @Test
    public void connectsToSQLite() throws Exception {
        try {
            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT 1")) {
                assertEquals(1, resultSet.getInt(1));
            }
        } finally {
            Files.deleteIfExists(DATABASE_FILE);
        }
    }

    @Test
    public void foreignKeysAreEnabledForEveryConnection() throws SQLException {
        try {
            assertForeignKeysEnabled();
            assertForeignKeysEnabled();
        } finally {
            try {
                Files.deleteIfExists(DATABASE_FILE);
            } catch (java.io.IOException exception) {
                throw new SQLException("Could not clean up test database", exception);
            }
        }
    }

    private void assertForeignKeysEnabled() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("PRAGMA foreign_keys")) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
        }
    }
}
