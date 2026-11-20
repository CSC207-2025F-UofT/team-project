package data_access.portfolio;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseInitializationTest {
    private static final Path DATABASE_FILE = Path.of("portfolio.db");

    @Test
    void initializeDatabaseCreatesAllTables() throws SQLException {
        try {
            DatabaseConnection.initializeDatabase();

            Set<String> tableNames = new HashSet<>();
            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(
                         "SELECT name FROM sqlite_master WHERE type = 'table'")) {
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString("name"));
                }
            }

            assertTrue(tableNames.contains("portfolios"));
            assertTrue(tableNames.contains("holdings"));
            assertTrue(tableNames.contains("transactions"));
        } finally {
            try {
                Files.deleteIfExists(DATABASE_FILE);
            } catch (java.io.IOException exception) {
                throw new SQLException("Could not clean up test database", exception);
            }
        }
    }
}
