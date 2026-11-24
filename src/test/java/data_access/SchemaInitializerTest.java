package data_access;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SchemaInitializerTest {
    @TempDir
    Path directory;

    @Test
    void initializesConfiguredFileAndPreservesRowsOnReinitialization() throws SQLException {
        Path file = directory.resolve("nested/game.db");
        var connections = new SqliteConnectionProvider(file);
        var initializer = new SchemaInitializer(connections);
        initializer.initialize();
        assertTrue(Files.isRegularFile(file));

        try (var connection = connections.openConnection(); var statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO users (username, password_hash) VALUES ('alice', 'hash')");
            statement.executeUpdate("INSERT INTO runs (run_id, username, status) VALUES ('r', 'alice', 'IN_PROGRESS')");
            statement.executeUpdate("""
                    INSERT INTO game_saves
                    (run_id, day, energy, sanity, academic_survival, money, current_location_id, saved_order)
                    VALUES ('r', 1, 100, 100, 50, 50, 'residence', 1)
                    """);
        }
        initializer.initialize();
        try (var connection = connections.openConnection(); var statement = connection.createStatement();
             var rows = statement.executeQuery("""
                     SELECT username, day FROM users JOIN runs USING (username) JOIN game_saves USING (run_id)
                     """)) {
            assertTrue(rows.next());
            assertEquals("alice", rows.getString("username"));
            assertEquals(1, rows.getInt("day"));
            assertFalse(rows.next());
        }
    }

    @Test
    void enforcesForeignKeysOnNewConnections() throws SQLException {
        var connections = new SqliteConnectionProvider(directory.resolve("game.db"));
        new SchemaInitializer(connections).initialize();
        try (var connection = connections.openConnection(); var statement = connection.createStatement()) {
            assertThrows(SQLException.class, () -> statement.executeUpdate(
                    "INSERT INTO runs (run_id, username, status) VALUES ('r', 'missing', 'IN_PROGRESS')"));
            assertThrows(SQLException.class, () -> statement.executeUpdate(
                    """
                    INSERT INTO game_saves
                    (run_id, day, energy, sanity, academic_survival, money, current_location_id, saved_order)
                    VALUES ('missing', 1, 100, 100, 50, 50, 'residence', 1)
                    """));
        }
    }
}
