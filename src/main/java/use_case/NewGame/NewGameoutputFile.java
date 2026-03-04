package use_case.NewGame;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Output data for the New Game use case.
 * This class is responsible for storing information
 * that will later be displayed by the presenter/view.
 */
public class NewGameoutputFile {

//    private final String username;
//    private final LocalDateTime gameStartTime;
//    private final int initialScore;
//    private final boolean success;
//    private final String message;
//
//    private final List<String> debugLogs;
//    private int difficultyLevel;
//    private long seed;
//
//    public NewGameOutputFile(String username,
//                             LocalDateTime gameStartTime,
//                             int initialScore,
//                             boolean success,
//                             String message) {
//        this.username = username;
//        this.gameStartTime = gameStartTime;
//        this.initialScore = initialScore;
//        this.success = success;
//        this.message = message;
//        this.debugLogs = new ArrayList<>();
//        this.difficultyLevel = 1;
//        this.seed = System.currentTimeMillis();
//    }
//
//    /* ----------------- Getters ----------------- */
//
//    public String getUsername() {
//        return username;
//    }
//
//    public LocalDateTime getGameStartTime() {
//        return gameStartTime;
//    }
//
//    public int getInitialScore() {
//        return initialScore;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public int getDifficultyLevel() {
//        return difficultyLevel;
//    }
//
//    public long getSeed() {
//        return seed;
//    }
//
//    /* ----------------- Setters ----------------- */
//
//    public void setDifficultyLevel(int difficultyLevel) {
//        if (difficultyLevel < 1) {
//            this.difficultyLevel = 1;
//        } else {
//            this.difficultyLevel = difficultyLevel;
//        }
//        addDebugLog("Difficulty set to " + this.difficultyLevel);
//    }
//
//    public void setSeed(long seed) {
//        this.seed = seed;
//        addDebugLog("Seed updated to " + seed);
//    }
//
//    /* ----------------- Debug Utilities ----------------- */
//
//    public void addDebugLog(String log) {
//        if (log != null && !log.isBlank()) {
//            debugLogs.add(LocalDateTime.now() + " :: " + log);
//        }
//    }
//
//    public List<String> getDebugLogs() {
//        return new ArrayList<>(debugLogs);
//    }
//
//    public void clearLogs() {
//        debugLogs.clear();
//    }
//
//    /* ----------------- Formatting Helpers ----------------- */
//
//    public String toDisplayString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("New Game Created\n");
//        builder.append("User: ").append(username).append("\n");
//        builder.append("Start Time: ").append(gameStartTime).append("\n");
//        builder.append("Initial Score: ").append(initialScore).append("\n");
//        builder.append("Difficulty: ").append(difficultyLevel).append("\n");
//        builder.append("Status: ").append(success ? "SUCCESS" : "FAILURE").append("\n");
//        builder.append("Message: ").append(message).append("\n");
//        return builder.toString();
//    }
//
//    public String toDebugString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("[DEBUG LOGS]\n");
//        for (String log : debugLogs) {
//            builder.append(log).append("\n");
//        }
//        return builder.toString();
//    }
//
//    /* ----------------- Validation ----------------- */
//
//    public boolean isValid() {
//        return username != null
//                && !username.isBlank()
//                && gameStartTime != null
//                && initialScore >= 0;
//    }
//
//    public boolean hasWarnings() {
//        return difficultyLevel > 5 || debugLogs.size() > 50;
//    }
//
//    /* ----------------- Utility Methods ----------------- */
//
//    public void resetToDefaults() {
//        clearLogs();
//        setDifficultyLevel(1);
//        addDebugLog("Reset to default values");
//    }
//
//    public boolean isHardMode() {
//        return difficultyLevel >= 4;
//    }
//
//    public boolean isEasyMode() {
//        return difficultyLevel <= 2;
//    }
//
//    public int computeStartingHealth() {
//        if (isHardMode()) {
//            return 50;
//        }
//        if (isEasyMode()) {
//            return 150;
//        }
//        return 100;
//    }
//
//    /* ----------------- Object Overrides ----------------- */
//
//    @Override
//    public String toString() {
//        return "NewGameOutputFile{" +
//                "username='" + username + '\'' +
//                ", gameStartTime=" + gameStartTime +
//                ", initialScore=" + initialScore +
//                ", success=" + success +
//                ", difficultyLevel=" + difficultyLevel +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof NewGameOutputFile)) return false;
//        NewGameOutputFile that = (NewGameOutputFile) o;
//        return initialScore == that.initialScore &&
//                success == that.success &&
//                difficultyLevel == that.difficultyLevel &&
//                Objects.equals(username, that.username) &&
//                Objects.equals(gameStartTime, that.gameStartTime);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(username, gameStartTime, initialScore, success, difficultyLevel);
//    }
//
//    /* ----------------- Mock / Placeholder Logic ----------------- */
//
//    public void simulateSetupSteps() {
//        addDebugLog("Initializing world");
//        addDebugLog("Loading player profile");
//        addDebugLog("Spawning entities");
//        addDebugLog("Finalizing setup");
//    }
//
//    public boolean isReadyToLaunch() {
//        return isValid() && success;
//    }
}
