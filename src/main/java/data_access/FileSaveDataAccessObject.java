package data_access;

import entity.LeaderboardEntry;
import use_case.leaderboard.LeaderboardDataAccessInterface;
import use_case.save.SaveDataAccessInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores each player's deepest floor in a CSV file, so saves survive closing the game.
 */
public class FileSaveDataAccessObject implements SaveDataAccessInterface,
                                                 LeaderboardDataAccessInterface {

    private static final String HEADER = "username, deepestFloor";

    private final File csvFile;
    private final Map<String, Integer> deepestFloors = new HashMap<>();

    /**
     * Opens the save file and loads any existing saves into memory.
     * If the file doesn't exist yet, we start empty; it's created on the first save.
     */
    public FileSaveDataAccessObject(String csvPath) {
        csvFile = new File(csvPath);
        if (csvFile.exists() && csvFile.length() > 0) {
            readFile();
        }
    }

    /** Fills the map from the file, one line (one player) at a time. */
    private void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String header = reader.readLine();
            if (!HEADER.equals(header)) {
                throw new RuntimeException("Unexpected header in " + csvFile + ": " + header);
            }

            String row;
            while ((row = reader.readLine()) != null) {  // readLine() returns null at the end
                if (row.isEmpty()) {                     // skip blank lines
                    continue;
                }
                String[] columns = row.split(","); // "darius, 5" -> ["darius", "5"]
                String username = columns[0];
                int floor = Integer.parseInt(columns[1]);
                deepestFloors.put(username, floor);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Rewrites the whole file from the map. */
    private void writeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(HEADER);
            writer.newLine();
            for (Map.Entry<String, Integer> entry : deepestFloors.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // ----- SaveDataAccessInterface -----

    @Override
    public boolean hasSave(String username) {
        return deepestFloors.containsKey(username);
    }

    @Override
    public int getDeepestFloor(String username) {
        return deepestFloors.get(username);
    }

    @Override
    public void saveDeepestFloor(String username, int floor) {
        deepestFloors.put(username, floor);
        writeFile();
    }

    // ----- LeaderboardDataAccessInterface -----

    @Override
    public List<LeaderboardEntry> getAllEntries() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : deepestFloors.entrySet()) {
            entries.add(new LeaderboardEntry(entry.getKey(), entry.getValue()));
        }
        return entries;
    }
}
