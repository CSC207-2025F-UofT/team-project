package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.Player;
import use_case.enter_name.makeNewPlayer.NewPlayerDataAccessInterface;
import use_case.enter_name.welcomeMenu.ReturningPlayerDataAccessInterface;
import use_case.finish_round.FinishRoundDataAccessInterface;
import use_case.view_stats.ViewStatsDataAccessInterface;

/**
 * Fake player storage backed by a HashMap. Nothing survives closing the app.
 * The real SQLite version must implement the same interfaces and behave the same
 * (e.g. get returns null for an unknown name).
 */
public class InMemoryPlayerDataAccessObject implements NewPlayerDataAccessInterface,
        ReturningPlayerDataAccessInterface, FinishRoundDataAccessInterface, ViewStatsDataAccessInterface {

    private final Map<String, Player> players = new HashMap<>();

    @Override
    public boolean existsByName(String name) {
        return players.containsKey(name);
    }

    @Override
    public Player get(String name) {
        return players.get(name);
    }

    @Override
    public void save(Player player) {
        players.put(player.getName(), player);
    }
}
