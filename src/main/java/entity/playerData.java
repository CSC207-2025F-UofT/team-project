package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player's account and stats.
 */
public class playerData {
    // ---------------- id / password ----------------
    private String id;
    private String password;

    // ---------------- data ----------------
    private int gamesPlayed;
    private int gameWin;
    private float winRate;
    private int unoCallsMade;
    
    // ---------------- achievements ----------------
    private final List<String> achievements = new ArrayList<>();

    public playerData(String id, String password) {
        setId(id);
        setPassword(password);
    }

    // ---------------- id / password ----------------
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be empty.");
        }
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = password;
    }

    // ---------------- data ----------------
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGameWin() {
        return gameWin;
    }

    public void setGameWin(int gameWin) {
        this.gameWin = gameWin;
    }

    public float getWinRate() {
        return (float) gameWin / gamesPlayed;
    }

    public int getUnoCallsMade() {
        return unoCallsMade;
    }

    public void setUnoCallsMade(int unoCallsMade) {
        this.unoCallsMade = unoCallsMade;
    }

    // ---------------- achievements ----------------
    public List<String> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }


    public void addAchievement(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Achievement name cannot be empty.");
        }
        if (!achievements.contains(name)) {
            achievements.add(name);
        }
    }

    public boolean hasAchievement(String name) {
        return achievements.contains(name);
    }
}
