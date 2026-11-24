package data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Opens independent connections. The caller must close every returned connection. */
public final class SqliteConnectionProvider {
    public static final Path DEFAULT_PATH = Path.of("data", "uofteart.db");

    private final Path path;

    public SqliteConnectionProvider() {
        this(DEFAULT_PATH);
    }

    public SqliteConnectionProvider(Path path) {
        this.path = path.toAbsolutePath().normalize();
    }

    public Connection openConnection() throws SQLException {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new SQLException("Could not create the SQLite directory for " + path, e);
        }

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try {
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
                statement.execute("PRAGMA busy_timeout = 5000");
            }
            return connection;
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException closeFailure) {
                e.addSuppressed(closeFailure);
            }
            throw e;
        }
    }
}
