package data_access;

import entity.playerData;
import use_case.login.LoginDataAccessInterface;
import use_case.signup.SignupDataAccessInterface;
import use_case.viewplayerstats.ViewPlayerStatsDataAccessInterface;
import use_case.viewplayerstats.ViewPlayerStatsSessionInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for player data implemented using a CSV file to persist the data.
 *
 * <p>Each line is one player: username, password, games played, games won, UNO calls made,
 * and achievements (separated by ";"). Text values are URL-encoded so that commas,
 * semicolons or line breaks inside a value can't break the file.</p>
 */
public class FilePlayerDataAccessObject implements LoginDataAccessInterface,
        SignupDataAccessInterface,
        ViewPlayerStatsDataAccessInterface,
        ViewPlayerStatsSessionInterface {

    private static final String HEADER = "username,password,games_played,game_win,uno_calls_made,achievements";
    private static final int COLUMNS = 6;

    private final File csvFile;
    private final Map<String, playerData> players = new LinkedHashMap<>();
    private String currentPlayerId;

    /**
     * Creates the DAO, loading any players already saved in the file.
     * If the file doesn't exist yet, it is created.
     *
     * @param csvPath path to the CSV file, e.g. "players.csv"
     * @throws IllegalStateException if the file exists but can't be read
     */
    public FilePlayerDataAccessObject(String csvPath) {
        this.csvFile = new File(csvPath);
        if (csvFile.exists() && csvFile.length() > 0) {
            load();
        }
        else {
            writeFile();
        }
    }

    private void load() {
        try (BufferedReader reader = Files.newBufferedReader(csvFile.toPath(), StandardCharsets.UTF_8)) {
            final String header = reader.readLine();
            if (!HEADER.equals(header)) {
                throw new IllegalStateException("Unexpected header in " + csvFile + ": " + header);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    final playerData player = parse(line);
                    players.put(player.getId(), player);
                }
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not read " + csvFile, e);
        }
    }

    private static playerData parse(String line) {
        final String[] col = line.split(",", -1);
        if (col.length != COLUMNS) {
            throw new IllegalStateException("Bad line in player file: " + line);
        }
        final playerData player = new playerData(decode(col[0]), decode(col[1]));
        player.setGamesPlayed(Integer.parseInt(col[2]));
        player.setGameWin(Integer.parseInt(col[3]));
        player.setUnoCallsMade(Integer.parseInt(col[4]));
        if (!col[5].isEmpty()) {
            for (String achievement : col[5].split(";")) {
                player.addAchievement(decode(achievement));
            }
        }
        return player;
    }

    private static String format(playerData player) {
        final List<String> achievements = new ArrayList<>();
        for (String achievement : player.getAchievements()) {
            achievements.add(encode(achievement));
        }
        return String.join(",",
                encode(player.getId()),
                encode(player.getPassword()),
                String.valueOf(player.getGamesPlayed()),
                String.valueOf(player.getGameWin()),
                String.valueOf(player.getUnoCallsMade()),
                String.join(";", achievements));
    }

    private void writeFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(csvFile.toPath(), StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();
            for (playerData player : players.values()) {
                writer.write(format(player));
                writer.newLine();
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not write " + csvFile, e);
        }
    }

    private static String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    private static String decode(String text) {
        return URLDecoder.decode(text, StandardCharsets.UTF_8);
    }

    /**
     * Checks whether an account with this username exists.
     *
     * @param username the username to look for
     * @return true if the account exists
     */
    @Override
    public boolean existsById(String username) {
        return players.containsKey(username);
    }

    /**
     * Saves (or updates) a player and writes every player to the file.
     *
     * @param player the account to save
     */
    @Override
    public void save(playerData player) {
        players.put(player.getId(), player);
        writeFile();
    }

    @Override
    public playerData getPlayerData(String username) {
        return players.get(username);
    }

    @Override
    public void setCurrentPlayerId(String username) {
        this.currentPlayerId = username;
    }

    @Override
    public String getCurrentPlayerId() {
        return currentPlayerId;
    }
}
