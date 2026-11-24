package data_access;

import entity.GameState;
import entity.GameStatus;
import entity.Player;
import use_case.GamePersistenceException;
import use_case.SaveGameDataAccessInterface;
import use_case.SavedGameSummary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class SqliteGameDataAccessObject implements SaveGameDataAccessInterface {
    private static final String OWNED_SAVES = """
            SELECT game_saves.*, runs.status
            FROM game_saves JOIN runs USING (run_id)
            WHERE runs.username = ?
            """;
    private static final String RECENT_FIRST = " ORDER BY saved_at DESC, saved_order DESC";
    private final SqliteConnectionProvider connections;

    public SqliteGameDataAccessObject(SqliteConnectionProvider connections) {
        this.connections = connections;
    }

    @Override
    public void save(String username, GameState game, String name) throws GamePersistenceException {
        try (Connection connection = connections.openConnection()) {
            connection.setAutoCommit(false);
            try {
                upsertRun(connection, username, game);
                upsertSnapshot(connection, game, name);
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackFailure) {
                    e.addSuppressed(rollbackFailure);
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new GamePersistenceException("Could not write the saved game", e);
        }
    }

    private void upsertRun(Connection connection, String username, GameState game) throws SQLException {
        try (var statement = connection.prepareStatement("""
                INSERT INTO runs (run_id, username, status) VALUES (?, ?, ?)
                ON CONFLICT(run_id) DO UPDATE SET status = excluded.status
                WHERE runs.username = excluded.username
                """)) {
            statement.setString(1, game.getRunId().toString());
            statement.setString(2, username);
            statement.setString(3, game.getStatus().name());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("The run belongs to a different account");
            }
        }
    }

    private void upsertSnapshot(Connection connection, GameState game, String name) throws SQLException {
        // The run upsert already holds SQLite's write lock. Each successful save
        // gets a higher order even when two saves happen within the same millisecond.
        try (var statement = connection.prepareStatement("""
                INSERT INTO game_saves
                    (run_id, day, energy, sanity, academic_survival, money, current_location_id,
                     saved_order, save_name, saved_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT COALESCE(MAX(saved_order), 0) + 1 FROM game_saves), ?, ?)
                ON CONFLICT(run_id) DO UPDATE SET
                    day = excluded.day, energy = excluded.energy, sanity = excluded.sanity,
                    academic_survival = excluded.academic_survival, money = excluded.money,
                    current_location_id = excluded.current_location_id, saved_order = excluded.saved_order,
                    saved_at = excluded.saved_at
                """)) {
            Player player = game.getPlayer();
            statement.setString(1, game.getRunId().toString());
            statement.setInt(2, game.getDay());
            statement.setInt(3, player.getEnergy());
            statement.setInt(4, player.getSanity());
            statement.setInt(5, player.getAcademicSurvival());
            statement.setInt(6, player.getMoney());
            statement.setString(7, game.getCurrentLocationId());
            statement.setString(8, name.isBlank() ? SavedGameSummary.defaultName(game.getRunId()) : name);
            statement.setLong(9, System.currentTimeMillis());
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<GameState> loadLatest(String username) throws GamePersistenceException {
        return readGame(username, OWNED_SAVES + RECENT_FIRST + " LIMIT 1", null);
    }

    @Override
    public Optional<GameState> load(String username, UUID runId) throws GamePersistenceException {
        return readGame(username, OWNED_SAVES + " AND game_saves.run_id = ?", runId);
    }

    private Optional<GameState> readGame(String username, String sql, UUID runId)
            throws GamePersistenceException {
        try (var connection = connections.openConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            if (runId != null) {
                statement.setString(2, runId.toString());
            }
            try (var row = statement.executeQuery()) {
                if (!row.next()) {
                    return Optional.empty();
                }
                Player player = new Player(row.getInt("energy"), row.getInt("sanity"),
                        row.getInt("academic_survival"), row.getInt("money"));
                return Optional.of(new GameState(UUID.fromString(row.getString("run_id")),
                        row.getInt("day"), player, row.getString("current_location_id"),
                        GameStatus.valueOf(row.getString("status"))));
            }
        } catch (SQLException | IllegalArgumentException e) {
            throw new GamePersistenceException("Could not read the saved game", e);
        }
    }

    @Override
    public List<SavedGameSummary> list(String username) throws GamePersistenceException {
        try (var connection = connections.openConnection();
             var statement = connection.prepareStatement(OWNED_SAVES + RECENT_FIRST)) {
            statement.setString(1, username);
            try (ResultSet rows = statement.executeQuery()) {
                List<SavedGameSummary> saves = new ArrayList<>();
                while (rows.next()) {
                    UUID runId = UUID.fromString(rows.getString("run_id"));
                    String name = rows.getString("save_name");
                    long timestamp = rows.getLong("saved_at");
                    Instant savedAt = rows.wasNull() ? null : Instant.ofEpochMilli(timestamp);
                    saves.add(new SavedGameSummary(runId, name.isBlank() ? SavedGameSummary.defaultName(runId) : name,
                            rows.getInt("day"), rows.getInt("energy"), rows.getInt("sanity"),
                            rows.getInt("academic_survival"), rows.getInt("money"), savedAt));
                }
                return saves;
            }
        } catch (SQLException | IllegalArgumentException e) {
            throw new GamePersistenceException("Could not list saved games", e);
        }
    }

    @Override
    public boolean delete(String username, UUID runId) throws GamePersistenceException {
        try (var connection = connections.openConnection();
             var statement = connection.prepareStatement("""
                     DELETE FROM game_saves WHERE run_id = ?
                     AND run_id IN (SELECT run_id FROM runs WHERE username = ?)
                     """)) {
            statement.setString(1, runId.toString());
            statement.setString(2, username);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new GamePersistenceException("Could not delete the saved game", e);
        }
    }
}
