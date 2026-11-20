package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.Player;
import use_case.enter_name.EnterNameDataAccessInterface;
import use_case.finish_round.FinishRoundDataAccessInterface;
import use_case.view_stats.ViewStatsDataAccessInterface;

/**
 * Fake player storage backed by a HashMap. Nothing survives closing the app.
 * The real SQLite version must implement the same three interfaces and behave the same
 * (e.g. getPlayer returns null for an unknown name).
 */
public class InMemoryPlayerDataAccessObject implements EnterNameDataAccessInterface,
        FinishRoundDataAccessInterface, ViewStatsDataAccessInterface {

    private final Map<String, Player> players = new HashMap<>();

    @Override
    public boolean playerExists(String name) {
        return players.containsKey(name);
    }

    @Override
    public Player getPlayer(String name) {
        return players.get(name);
    }

    @Override
    public void savePlayer(Player player) {
        players.put(player.getName(), player);
    }
}
