package data_access;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;
import entity.WeatherCondition;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.continue_game.ContinueGameDataAccessInterface;
import use_case.continue_game.InvalidCheckpointException;
import use_case.rest.RestGameStateDataAccessInterface;
import use_case.rest.SaveFailedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Holds the current game state and saves it to / loads it from a JSON checkpoint file.
 * Only completed progress is saved; the academic record is to be added once the team's entities exist.
 */
public class FileGameStateDataAccessObject
        implements RestGameStateDataAccessInterface, ContinueGameDataAccessInterface {
    static final int FORMAT_VERSION = 1;

    private final Path file;
    private GameDay currentDay;
    private Player player;
    private DailyWeather currentWeather;

    /**
     * @param file where the checkpoint is stored
     * @param currentDay the day a new game starts on
     * @param player the player a new game starts with
     */
    public FileGameStateDataAccessObject(Path file, GameDay currentDay, Player player) {
        this.file = file;
        this.currentDay = currentDay;
        this.player = player;
    }

    @Override
    public void saveCheckpoint() throws SaveFailedException {
        JSONObject json = new JSONObject()
                .put("version", FORMAT_VERSION)
                .put("dayNumber", currentDay.getDayNumber())
                .put("startDate", currentDay.getStartDate().toString())
                .put("energy", player.getEnergy())
                .put("maxEnergy", player.getMaxEnergy());
        if (currentWeather != null) {
            json.put("weather", new JSONObject()
                    .put("date", currentWeather.getDate().toString())
                    .put("condition", currentWeather.getCondition().name())
                    .put("high", currentWeather.getHighTemperature())
                    .put("low", currentWeather.getLowTemperature()));
        }
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            // Write to a temporary file first so a failed save never damages the previous checkpoint.
            Path temp = file.resolveSibling(file.getFileName() + ".tmp");
            Files.writeString(temp, json.toString(2), StandardCharsets.UTF_8);
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new SaveFailedException("Could not save progress to " + file, e);
        }
    }

    @Override
    public boolean hasCheckpoint() {
        return Files.isRegularFile(file);
    }

    @Override
    public void loadCheckpoint() throws InvalidCheckpointException {
        try {
            JSONObject json = new JSONObject(Files.readString(file, StandardCharsets.UTF_8));
            if (json.getInt("version") != FORMAT_VERSION) {
                throw new InvalidCheckpointException("Unsupported checkpoint version: " + json.getInt("version"));
            }
            GameDay day = new GameDay(json.getInt("dayNumber"), LocalDate.parse(json.getString("startDate")));
            int energy = json.getInt("energy");
            int maxEnergy = json.getInt("maxEnergy");
            if (maxEnergy <= 0 || energy < 0 || energy > maxEnergy) {
                throw new InvalidCheckpointException("Invalid energy in checkpoint");
            }
            DailyWeather weather = null;
            if (json.has("weather")) {
                JSONObject w = json.getJSONObject("weather");
                weather = new DailyWeather(LocalDate.parse(w.getString("date")),
                        WeatherCondition.valueOf(w.getString("condition")),
                        w.getDouble("high"), w.getDouble("low"));
            }
            // Only replace the state once everything has been read successfully.
            currentDay = day;
            player = new Player(energy, maxEnergy);
            currentWeather = weather;
        } catch (IOException | JSONException | DateTimeException | IllegalArgumentException e) {
            throw new InvalidCheckpointException("Checkpoint is damaged: " + e.getMessage(), e);
        }
    }

    @Override
    public GameDay getCurrentDay() {
        return currentDay;
    }

    @Override
    public void setCurrentDay(GameDay day) {
        this.currentDay = day;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public DailyWeather getCurrentWeather() {
        return currentWeather;
    }

    @Override
    public void setCurrentWeather(DailyWeather weather) {
        this.currentWeather = weather;
    }
}
