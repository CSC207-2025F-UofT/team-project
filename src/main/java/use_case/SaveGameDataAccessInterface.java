package use_case;

import entity.GameState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Stores snapshots independently of the currently active game. */
public interface SaveGameDataAccessInterface {
    default void save(String username, GameState game) throws GamePersistenceException {
        save(username, game, "");
    }

    void save(String username, GameState game, String name) throws GamePersistenceException;

    Optional<GameState> loadLatest(String username) throws GamePersistenceException;

    Optional<GameState> load(String username, UUID runId) throws GamePersistenceException;

    List<SavedGameSummary> list(String username) throws GamePersistenceException;

    boolean delete(String username, UUID runId) throws GamePersistenceException;
}
