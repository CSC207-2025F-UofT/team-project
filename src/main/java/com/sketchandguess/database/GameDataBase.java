package com.sketchandguess.database;

import com.sketchandguess.entities.GameRecord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameDataBase implements DataBase {
    // The database storing each game.
    // The string contains the prompt used for the game, and helps us search for specific prompts.
    public Map<Integer, GameRecord> GameData;

    public GameDataBase() {
        this.GameData = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\games.csv"));

            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split("|");
                GameRecord currentGame = new GameRecord(); // TODO: implement this once gameRecord constructor is implemented
                GameData.put(Integer.parseInt(parts[0]), currentGame);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ;
    }

    // overloaded version for search function
    public GameDataBase(HashMap<Integer, GameRecord> GameData) {
        this.GameData = GameData;
    }

    public GameRecord GetGame(Integer GameCode) {
        return GameData.get(GameCode);
    }

    public GameDataBase SearchWord(String Query) {
        HashMap<Integer, GameRecord> Matches = new HashMap<>();
        for (GameRecord g: this.GameData.values()) {
            // TODO come back to this once gamerecord is implemented
            if (g.getPrompt().contains(Query)) {
                Matches.put(Matches.size(), g);
            }
        }
        return new GameDataBase(Matches);
    }
}