package data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC implementation of the watchlist repository using the `watched_stocks` table.
 *
 * Expected schema (created in TableInitializer):
 *
 * CREATE TABLE IF NOT EXISTS watched_stocks (
 *     id          INTEGER PRIMARY KEY AUTOINCREMENT,
 *     username    TEXT NOT NULL,
 *     symbol      TEXT NOT NULL,
 *     name        TEXT,
 *     exchange    TEXT,
 *     created_at  TEXT NOT NULL DEFAULT (datetime('now')),
 *     UNIQUE(username, symbol)
 * );
 */
public class JdbcWatchlistRepository implements WatchlistRepository {

    private final DataSource dataSource;

    public JdbcWatchlistRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean isWatched(String username, String symbol) {
        final String sql = "SELECT 1 FROM watched_stocks WHERE username = ? AND symbol = ? LIMIT 1";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, symbol);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            // Wrap checked exception in an unchecked one so callers don't need to deal with SQL here
            throw new RuntimeException("Failed to check watchlist for user=" + username
                    + " symbol=" + symbol, e);
        }
    }

    @Override
    public void addWatched(String username, String symbol, String name, String exchange) {
        final String sql = "INSERT OR IGNORE INTO watched_stocks (username, symbol, name, exchange) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, symbol);
            ps.setString(3, name);
            ps.setString(4, exchange);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add watched stock for user=" + username
                    + " symbol=" + symbol, e);
        }
    }

    @Override
    public void removeWatched(String username, String symbol) {
        final String sql = "DELETE FROM watched_stocks WHERE username = ? AND symbol = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, symbol);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove watched stock for user=" + username
                    + " symbol=" + symbol, e);
        }
    }
}