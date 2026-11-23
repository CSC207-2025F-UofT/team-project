package data_access;

import entity.Player;
import entity.PlayerFactory;
import use_case.enter_name.welcomeMenu.ReturningPlayerDataAccessInterface;
import use_case.enter_name.makeNewPlayer.NewPlayerDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for player data implemented using a File to persist the data.
 */
public class PlayerDataAccessObject implements NewPlayerDataAccessInterface, ReturningPlayerDataAccessInterface {
    private static final String HEADER = "username";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, Player> accounts = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param playerFactory factory for creating player objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public PlayerDataAccessObject(String csvPath, PlayerFactory playerFactory) {
        csvFile = new File(csvPath);
        headers.put("username", 0);

        if (csvFile.length() == 0) {
            save();
        }
        else {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String username = row;
                    final Player player = playerFactory.create(username);
                    accounts.put(username, player);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(HEADER);
            writer.newLine();

            for (Player player : accounts.values()) {
                writer.write(player.getName());
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public void save(Player player) {
        accounts.put(player.getName(), player);
        this.save();
    }

    @Override
    public Player get(String username) {
        return accounts.get(username);
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }
}
