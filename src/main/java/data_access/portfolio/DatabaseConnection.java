package data_access.portfolio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Opens connections to the portfolio SQLite database.
 */
public final class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:portfolio.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException exception) {
            connection.close();
            throw exception;
        }
        return connection;
    }

    public static void initializeDatabase() throws SQLException {
        String createPortfolios =
                "CREATE TABLE IF NOT EXISTS portfolios ("
                        + "username TEXT PRIMARY KEY, "
                        + "available_cash NUMERIC NOT NULL"
                        + ")";
        String createHoldings =
                "CREATE TABLE IF NOT EXISTS holdings ("
                        + "username TEXT NOT NULL, "
                        + "ticker TEXT NOT NULL, "
                        + "quantity INTEGER NOT NULL, "
                        + "average_purchase_price NUMERIC NOT NULL, "
                        + "current_price NUMERIC NOT NULL, "
                        + "PRIMARY KEY (username, ticker), "
                        + "FOREIGN KEY (username) REFERENCES portfolios(username)"
                        + ")";
        String createTransactions =
                "CREATE TABLE IF NOT EXISTS transactions ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "username TEXT NOT NULL, "
                        + "ticker TEXT NOT NULL, "
                        + "type TEXT NOT NULL, "
                        + "quantity INTEGER NOT NULL, "
                        + "price NUMERIC NOT NULL, "
                        + "timestamp TEXT NOT NULL, "
                        + "FOREIGN KEY (username) REFERENCES portfolios(username)"
                        + ")";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createPortfolios);
            statement.execute(createHoldings);
            statement.execute(createTransactions);
        }
    }
}
