package data_access;

import entity.GameState;
import entity.GameStatus;
import entity.Outcome;
import entity.Player;
import entity.StatEffects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.GamePersistenceException;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqliteGameDataAccessObjectTest {
    @TempDir
    Path directory;

    private Path file;
    private SqliteConnectionProvider connections;
    private SqliteGameDataAccessObject saves;

    @BeforeEach
    void initialize() throws Exception {
        file = directory.resolve("game.db");
        connections = new SqliteConnectionProvider(file);
        new SchemaInitializer(connections).initialize();
        saves = new SqliteGameDataAccessObject(connections);
        try (var connection = connections.openConnection(); var statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO users (username, password_hash) VALUES ('alice', 'a'), ('bob', 'b')");
        }
    }

    @Test
    void restoresSnapshotAfterReopenAndUpdatesLoadedRun() throws Exception {
        var game = new GameState(4, new Player(86, 67, 78, 151), "robarts");
        saves.save("alice", game);
        game.apply(new Outcome.Builder().effects(new StatEffects(-86, -4, 4, -30))
                .moveTo("bahen").build());

        var reopened = new SqliteGameDataAccessObject(new SqliteConnectionProvider(file));
        GameState loaded = reopened.loadLatest("alice").orElseThrow();
        assertEquals(game.getRunId(), loaded.getRunId());
        assertEquals(4, loaded.getDay());
        assertEquals(86, loaded.getPlayer().getEnergy());
        assertEquals(67, loaded.getPlayer().getSanity());
        assertEquals(78, loaded.getPlayer().getAcademicSurvival());
        assertEquals(151, loaded.getPlayer().getMoney());
        assertEquals("robarts", loaded.getCurrentLocationId());
        assertEquals(GameStatus.IN_PROGRESS, loaded.getStatus());

        reopened.save("alice", game);
        loaded = reopened.loadLatest("alice").orElseThrow();
        assertEquals(GameStatus.LOST, loaded.getStatus());
        assertEquals("bahen", loaded.getCurrentLocationId());
        reopened.save("alice", loaded);
        try (var connection = connections.openConnection(); var statement = connection.createStatement();
             var result = statement.executeQuery("SELECT (SELECT COUNT(*) FROM runs), COUNT(*) FROM game_saves")) {
            assertTrue(result.next());
            assertEquals(1, result.getInt(1));
            assertEquals(1, result.getInt(2));
        }
    }

    @Test
    void selectsLatestSaveForEachAccountAndRejectsCrossAccountOverwrite() throws Exception {
        GameState first = GameState.startNew();
        GameState second = GameState.startNew();
        assertTrue(saves.loadLatest("alice").isEmpty());
        saves.save("alice", first);
        assertTrue(saves.loadLatest("bob").isEmpty());
        saves.save("bob", second);
        assertThrows(GamePersistenceException.class, () -> saves.save("bob", first));
        GameState next = GameState.startNew();
        saves.save("alice", next);
        assertEquals(next.getRunId(), saves.loadLatest("alice").orElseThrow().getRunId());
        saves.save("alice", first);
        assertEquals(first.getRunId(), saves.loadLatest("alice").orElseThrow().getRunId());
        assertEquals(second.getRunId(), saves.loadLatest("bob").orElseThrow().getRunId());
    }

    @Test
    void rollsBackRunWhenSnapshotCannotBeSaved() throws Exception {
        var invalid = new GameState(0, Player.createNew(), "residence");
        assertThrows(GamePersistenceException.class, () -> saves.save("alice", invalid));
        assertTrue(saves.loadLatest("alice").isEmpty());
        try (var connection = connections.openConnection(); var statement = connection.createStatement();
             var result = statement.executeQuery("SELECT COUNT(*) FROM runs")) {
            assertTrue(result.next());
            assertEquals(0, result.getInt(1));
        }
    }

    @Test
    void listsOwnedRunsAndUpdatesSelectedSnapshotWithoutRenamingIt() throws Exception {
        var first = new GameState(2, new Player(73, 80, 55, 44), "residence");
        var second = new GameState(5, new Player(22, 41, 66, 95), "robarts");
        assertTrue(saves.list("alice").isEmpty());
        saves.save("alice", first, "First Run");
        saves.save("alice", second, "");
        saves.save("bob", GameState.startNew(), "Private Run");

        var reopened = new SqliteGameDataAccessObject(new SqliteConnectionProvider(file));
        var listed = reopened.list("alice");
        assertEquals(List.of(second.getRunId(), first.getRunId()), listed.stream().map(row -> row.runId()).toList());
        assertEquals("Game " + second.getRunId().toString().substring(0, 8), listed.get(0).name());
        var firstSummary = listed.get(1);
        assertEquals("First Run", firstSummary.name());
        assertEquals(List.of(2, 73, 80, 55, 44), List.of(firstSummary.day(), firstSummary.energy(),
                firstSummary.sanity(), firstSummary.academicSurvival(), firstSummary.money()));
        assertNotNull(firstSummary.savedAt());
        assertTrue(reopened.load("bob", first.getRunId()).isEmpty());

        try (var connection = connections.openConnection(); var statement = connection.prepareStatement(
                "UPDATE game_saves SET saved_at = 1 WHERE run_id = ?")) {
            statement.setString(1, first.getRunId().toString());
            statement.executeUpdate();
        }
        assertEquals(Instant.ofEpochMilli(1), reopened.list("alice").get(1).savedAt());
        var selected = reopened.load("alice", first.getRunId()).orElseThrow();
        assertEquals(2, selected.getDay());
        assertEquals(73, selected.getPlayer().getEnergy());
        selected.apply(new Outcome.Builder().effects(new StatEffects(-9, 0, 0, 0)).build());
        reopened.save("alice", selected, "Ignored rename");
        var refreshed = reopened.list("alice");
        assertEquals(2, refreshed.size());
        assertEquals(first.getRunId(), refreshed.get(0).runId());
        assertEquals("First Run", refreshed.get(0).name());
        assertEquals(64, refreshed.get(0).energy());
        assertTrue(refreshed.get(0).savedAt().isAfter(Instant.ofEpochMilli(1)));
        assertEquals(listed.get(0), refreshed.get(1));
    }

    @Test
    void deletesOnlyOwnedSnapshotAndAllowsSavingTheActiveRunAgain() throws Exception {
        var first = GameState.startNew();
        var second = GameState.startNew();
        saves.save("alice", first, "First");
        saves.save("alice", second, "Second");
        assertFalse(saves.delete("bob", first.getRunId()));
        assertTrue(saves.delete("alice", first.getRunId()));
        assertFalse(saves.delete("alice", first.getRunId()));
        var reopened = new SqliteGameDataAccessObject(new SqliteConnectionProvider(file));
        assertEquals(second.getRunId(), reopened.list("alice").get(0).runId());
        assertEquals(1, reopened.list("alice").size());
        assertTrue(reopened.load("alice", first.getRunId()).isEmpty());
        try (var connection = connections.openConnection(); var statement = connection.createStatement();
             var result = statement.executeQuery("SELECT COUNT(*) FROM runs")) {
            assertTrue(result.next());
            assertEquals(2, result.getInt(1));
        }
        reopened.save("alice", first, "Recreated");
        assertEquals(first.getRunId(), reopened.list("alice").get(0).runId());
        assertEquals("Recreated", reopened.list("alice").get(0).name());
    }

    @Test
    void migratesLegacySnapshotsWithoutLosingData() throws Exception {
        var run = GameState.startNew();
        saves.save("alice", run);
        try (var connection = connections.openConnection(); var statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE game_saves DROP COLUMN save_name");
            statement.executeUpdate("ALTER TABLE game_saves DROP COLUMN saved_at");
        }
        var initializer = new SchemaInitializer(connections);
        initializer.initialize();
        initializer.initialize();
        var legacy = saves.list("alice").get(0);
        assertEquals(run.getRunId(), legacy.runId());
        assertEquals(100, legacy.energy());
        assertNull(legacy.savedAt());
        assertFalse(legacy.name().isBlank());
        saves.save("alice", run);
        assertEquals(legacy.name(), saves.list("alice").get(0).name());
        assertNotNull(saves.list("alice").get(0).savedAt());
    }
}
