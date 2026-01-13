package data_access;

import entity.*;
import use_case.Battle.BattleUserDataAccessInterface;
import use_case.move.MoveGameDataAccessInterface;
import use_case.show_results.ShowResultsGameDataAccessInterface;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileGameDataAccessObject implements MoveGameDataAccessInterface, ShowResultsGameDataAccessInterface, BattleUserDataAccessInterface {

    // Battle state tracking
    private User userBeforeBattle;
    private User userAfterBattle;
    private Monster monsterBeforeBattle;
    private Monster monsterAfterBattle;

    private AdventureGame game;
    private final FileDataAccess fileDataAccess;
    private static final String FILE_PATH = "userdata.json";

    public FileGameDataAccessObject() {
        this.fileDataAccess = new FileDataAccess();

        // Check if userdata.json exists and is not empty
        File file = new File(FILE_PATH);
        if (file.exists() && file.length() > 0) {
            // Attempt to load existing game
            this.game = fileDataAccess.load(AdventureGame.class);
        }

        // If game is still null (file empty, doesn't exist, or couldn't be parsed), start new game
        if (this.game == null) {
            startNewGame();
        }
    }

    private void startNewGame() {
        User user = new User();
        Location loc0 = new Location("Bahen Centre for Information Technology", 43.6594, -79.3981, null);
        Location loc1 = new Location("Myhal Centre For Engineering Innovation & Entrepreneurship", 43.6606, -79.3966, new Monster());
        Location loc2 = new Location("Gerstein Science Information Centre", 43.6624, -79.3940, null);

        List<Location> locations = Arrays.asList(loc0, loc1, loc2);

        GameMap gameMap = new GameMap(locations, 0);
        this.game = new AdventureGame(user, gameMap);
    }

    @Override
    public AdventureGame getGame() {
        return this.game;
    }

    @Override
    public AdventureGame get() {
        return this.game;
    }

    @Override
    public void saveGame(AdventureGame game) {
        this.game = game;
        fileDataAccess.save(game);
    }

    @Override
    public void clearGameData() {
        File file = new File(FILE_PATH);
        System.out.println("Attempting to clear game data...");
        System.out.println("File exists: " + file.exists());
        System.out.println("File path: " + file.getAbsolutePath());
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("File deleted: " + deleted);
            if (!deleted) {
                System.err.println("Failed to delete file!");
            }
        }
        // Reset to a new game in memory
        startNewGame();
    }

    // BattleUserDataAccessInterface implementation

    @Override
    public void save(User user, Monster monster) {
        // If this is the first save (before battle starts)
        if (userBeforeBattle == null) {
            userBeforeBattle = user;
            monsterBeforeBattle = monster;
        }
        // Always update the "after" state
        userAfterBattle = user;
        monsterAfterBattle = monster;
    }

    @Override
    public User getUserBeforeBattle() {
        return userBeforeBattle;
    }

    @Override
    public User getUserAfterBattle() {
        return userAfterBattle;
    }

    @Override
    public Monster getMonsterBeforeBattle() {
        return monsterBeforeBattle;
    }

    @Override
    public Monster getMonsterAfterBattle() {
        return monsterAfterBattle;
    }
}
