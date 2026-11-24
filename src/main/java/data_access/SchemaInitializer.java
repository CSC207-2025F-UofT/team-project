package data_access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Creates the shared Phase 1 tables at startup. */
public final class SchemaInitializer {
    private final SqliteConnectionProvider connections;

    public SchemaInitializer(SqliteConnectionProvider connections) {
        this.connections = connections;
    }

    public void initialize() throws SQLException {
        try (Connection connection = connections.openConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        username TEXT PRIMARY KEY NOT NULL,
                        password_hash TEXT NOT NULL,
                        salt TEXT
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS runs (
                        run_id TEXT PRIMARY KEY NOT NULL,
                        username TEXT NOT NULL REFERENCES users(username),
                        status TEXT NOT NULL,
                        started_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS game_saves (
                        run_id TEXT PRIMARY KEY NOT NULL REFERENCES runs(run_id),
                        day INTEGER NOT NULL CHECK (day BETWEEN 1 AND 7),
                        energy INTEGER NOT NULL CHECK (energy BETWEEN 0 AND 100),
                        sanity INTEGER NOT NULL CHECK (sanity BETWEEN 0 AND 100),
                        academic_survival INTEGER NOT NULL CHECK (academic_survival BETWEEN 0 AND 100),
                        money INTEGER NOT NULL CHECK (money >= 0),
                        current_location_id TEXT,
                        saved_order INTEGER NOT NULL
                    )
                    """);
            statement.executeUpdate("CREATE INDEX IF NOT EXISTS runs_by_username ON runs(username)");
            addSaveColumn(statement, "save_name", "TEXT NOT NULL DEFAULT ''");
            addSaveColumn(statement, "saved_at", "INTEGER");
            connection.commit();
        }
    }

    private void addSaveColumn(Statement statement, String name, String definition) throws SQLException {
        try (var columns = statement.executeQuery("PRAGMA table_info(game_saves)")) {
            while (columns.next()) {
                if (name.equals(columns.getString("name"))) {
                    return;
                }
            }
        }
        statement.executeUpdate("ALTER TABLE game_saves ADD COLUMN " + name + " " + definition);
    }
}
