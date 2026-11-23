package data_access;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;
import entity.WeatherCondition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.continue_game.InvalidCheckpointException;
import use_case.rest.SaveFailedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FileGameStateDataAccessObjectTest {
    private static final LocalDate START = LocalDate.of(2025, 9, 2);

    @TempDir
    Path dir;

    private FileGameStateDataAccessObject newGame(Path file) {
        return new FileGameStateDataAccessObject(file, new GameDay(1, START), new Player(100, 100));
    }

    @Test
    void savedCheckpointCanBeLoadedBack() throws Exception {
        Path file = dir.resolve("checkpoint.json");
        FileGameStateDataAccessObject original = newGame(file);
        original.setCurrentDay(new GameDay(3, START));
        original.getPlayer().restoreEnergyToPercent(0);
        original.setCurrentWeather(new DailyWeather(START.plusDays(2), WeatherCondition.SNOW, -1.5, -7.0));
        original.saveCheckpoint();

        FileGameStateDataAccessObject loaded = newGame(file);
        assertTrue(loaded.hasCheckpoint());
        loaded.loadCheckpoint();

        assertEquals(3, loaded.getCurrentDay().getDayNumber());
        assertEquals(START, loaded.getCurrentDay().getStartDate());
        assertEquals(100, loaded.getPlayer().getEnergy());
        assertEquals(100, loaded.getPlayer().getMaxEnergy());
        assertEquals(WeatherCondition.SNOW, loaded.getCurrentWeather().getCondition());
        assertEquals(START.plusDays(2), loaded.getCurrentWeather().getDate());
        assertEquals(-1.5, loaded.getCurrentWeather().getHighTemperature());
        assertEquals(-7.0, loaded.getCurrentWeather().getLowTemperature());
    }

    @Test
    void checkpointWithoutWeatherRoundTrips() throws Exception {
        Path file = dir.resolve("checkpoint.json");
        newGame(file).saveCheckpoint();

        FileGameStateDataAccessObject loaded = newGame(file);
        loaded.loadCheckpoint();

        assertNull(loaded.getCurrentWeather());
    }

    @Test
    void noCheckpointWhenFileIsMissing() {
        assertFalse(newGame(dir.resolve("missing.json")).hasCheckpoint());
    }

    @Test
    void damagedCheckpointIsRejectedAndLeftUnchanged() throws Exception {
        Path file = dir.resolve("checkpoint.json");
        Files.writeString(file, "{not json");

        assertThrows(InvalidCheckpointException.class, () -> newGame(file).loadCheckpoint());
        assertEquals("{not json", Files.readString(file));
    }

    @Test
    void unsupportedVersionIsRejected() throws Exception {
        Path file = dir.resolve("checkpoint.json");
        Files.writeString(file, "{\"version\":99,\"dayNumber\":1,\"startDate\":\"2025-09-02\","
                + "\"energy\":50,\"maxEnergy\":100}");

        assertThrows(InvalidCheckpointException.class, () -> newGame(file).loadCheckpoint());
    }

    @Test
    void impossibleValuesAreRejected() throws Exception {
        Path file = dir.resolve("checkpoint.json");
        Files.writeString(file, "{\"version\":1,\"dayNumber\":1,\"startDate\":\"2025-09-02\","
                + "\"energy\":500,\"maxEnergy\":100}");

        assertThrows(InvalidCheckpointException.class, () -> newGame(file).loadCheckpoint());
    }

    @Test
    void saveFailsWhenLocationIsNotWritable() throws Exception {
        Path blocker = dir.resolve("blocker");
        Files.writeString(blocker, "a file, not a directory");

        assertThrows(SaveFailedException.class, () -> newGame(blocker.resolve("checkpoint.json")).saveCheckpoint());
    }
}
