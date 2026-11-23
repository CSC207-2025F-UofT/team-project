package use_case.continue_game;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;
import entity.WeatherCondition;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContinueGameInteractorTest {
    private static final LocalDate START = LocalDate.of(2025, 9, 2);

    private static class FakeSave implements ContinueGameDataAccessInterface {
        boolean exists = true;
        boolean damaged;
        DailyWeather savedWeather = new DailyWeather(START.plusDays(4), WeatherCondition.CLOUDY, 18.0, 9.0);
        GameDay day = new GameDay(1, START);
        Player player = new Player(100, 100);
        DailyWeather weather;

        public boolean hasCheckpoint() { return exists; }

        public void loadCheckpoint() throws InvalidCheckpointException {
            if (damaged) {
                throw new InvalidCheckpointException("bad file");
            }
            day = new GameDay(5, START);
            player = new Player(60, 100);
            weather = savedWeather;
        }

        public GameDay getCurrentDay() { return day; }
        public Player getPlayer() { return player; }
        public DailyWeather getCurrentWeather() { return weather; }
    }

    private static class CapturingPresenter implements ContinueGameOutputBoundary {
        ContinueGameOutputData output;
        String failMessage;
        public void prepareSuccessView(ContinueGameOutputData output) { this.output = output; }
        public void prepareFailView(String message) { this.failMessage = message; }
    }

    @Test
    void restoresSavedDayWeatherAndEnergy() {
        CapturingPresenter presenter = new CapturingPresenter();

        new ContinueGameInteractor(new FakeSave(), presenter).execute();

        assertNull(presenter.failMessage);
        assertEquals(5, presenter.output.getDayNumber());
        assertEquals(START.plusDays(4), presenter.output.getDate());
        assertTrue(presenter.output.hasWeather());
        assertEquals(WeatherCondition.CLOUDY, presenter.output.getCondition());
        assertEquals(18.0, presenter.output.getHighTemperature());
        assertEquals(60, presenter.output.getEnergy());
        assertEquals(100, presenter.output.getMaxEnergy());
    }

    @Test
    void checkpointWithoutWeatherIsRestored() {
        FakeSave save = new FakeSave();
        save.savedWeather = null;
        CapturingPresenter presenter = new CapturingPresenter();

        new ContinueGameInteractor(save, presenter).execute();

        assertFalse(presenter.output.hasWeather());
    }

    @Test
    void missingCheckpointExplainsAndOffersNewGame() {
        FakeSave save = new FakeSave();
        save.exists = false;
        CapturingPresenter presenter = new CapturingPresenter();

        new ContinueGameInteractor(save, presenter).execute();

        assertNull(presenter.output);
        assertEquals(ContinueGameInteractor.NO_CHECKPOINT_MESSAGE, presenter.failMessage);
    }

    @Test
    void damagedCheckpointIsReportedAndStateIsUnchanged() {
        FakeSave save = new FakeSave();
        save.damaged = true;
        CapturingPresenter presenter = new CapturingPresenter();

        new ContinueGameInteractor(save, presenter).execute();

        assertNull(presenter.output);
        assertEquals(ContinueGameInteractor.INVALID_CHECKPOINT_MESSAGE, presenter.failMessage);
        assertEquals(1, save.getCurrentDay().getDayNumber());
    }
}
