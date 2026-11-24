package use_case.rest;

import entity.DailyWeather;
import entity.GameDay;
import entity.Player;
import entity.WeatherCondition;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RestInteractorTest {
    private static final LocalDate START = LocalDate.of(2025, 9, 2);

    private static class FakeGameState implements RestGameStateDataAccessInterface {
        GameDay day = new GameDay(1, START);
        final Player player = new Player(20, 100);
        DailyWeather weather;
        boolean failSave;
        int saveCount;
        int savedDayNumber;
        DailyWeather savedWeather;

        public GameDay getCurrentDay() { return day; }
        public void setCurrentDay(GameDay day) { this.day = day; }
        public Player getPlayer() { return player; }
        public void setCurrentWeather(DailyWeather weather) { this.weather = weather; }

        public void saveCheckpoint() throws SaveFailedException {
            saveCount++;
            if (failSave) {
                throw new SaveFailedException("disk full", null);
            }
            savedDayNumber = day.getDayNumber();
            savedWeather = weather;
        }
    }

    private static RestWeatherDataAccessInterface weatherOf(WeatherCondition condition) {
        return date -> new DailyWeather(date, condition, 5.0, -2.0);
    }

    private static class CapturingPresenter implements RestOutputBoundary {
        RestOutputData output;
        Boolean retryResult;
        String failMessage;
        public void prepareSuccessView(RestOutputData output) { this.output = output; }
        public void prepareFailView(String message) { this.failMessage = message; }
        public void prepareSaveRetryView(boolean saved) { this.retryResult = saved; }
    }

    @Test
    void restAdvancesDayAndRestoresEnergyByWeather() {
        FakeGameState state = new FakeGameState();
        CapturingPresenter presenter = new CapturingPresenter();

        new RestInteractor(state, weatherOf(WeatherCondition.RAIN), presenter).execute();

        assertEquals(2, state.getCurrentDay().getDayNumber());
        assertEquals(85, state.getPlayer().getEnergy());
        assertEquals(2, presenter.output.getDayNumber());
        assertEquals(START.plusDays(1), presenter.output.getDate());
        assertEquals(WeatherCondition.RAIN, presenter.output.getCondition());
        assertEquals(85, presenter.output.getEnergy());
        assertEquals(100, presenter.output.getMaxEnergy());
    }

    @Test
    void weatherIsLookedUpForTheNewDaysRealDate() {
        FakeGameState state = new FakeGameState();
        LocalDate[] requested = new LocalDate[1];
        RestWeatherDataAccessInterface weather = date -> {
            requested[0] = date;
            return new DailyWeather(date, WeatherCondition.CLEAR, 20.0, 10.0);
        };

        new RestInteractor(state, weather, new CapturingPresenter()).execute();

        assertEquals(START.plusDays(1), requested[0]);
    }

    @Test
    void newDayAndItsWeatherAreSaved() {
        FakeGameState state = new FakeGameState();
        CapturingPresenter presenter = new CapturingPresenter();

        new RestInteractor(state, weatherOf(WeatherCondition.SNOW), presenter).execute();

        assertEquals(1, state.saveCount);
        assertEquals(2, state.savedDayNumber);
        assertEquals(WeatherCondition.SNOW, state.savedWeather.getCondition());
        assertTrue(presenter.output.isSaved());
    }

    @Test
    void failedSaveKeepsNewDayAndReportsNotSaved() {
        FakeGameState state = new FakeGameState();
        state.failSave = true;
        CapturingPresenter presenter = new CapturingPresenter();

        new RestInteractor(state, weatherOf(WeatherCondition.CLEAR), presenter).execute();

        assertEquals(2, state.getCurrentDay().getDayNumber());
        assertEquals(100, state.getPlayer().getEnergy());
        assertFalse(presenter.output.isSaved());
    }

    @Test
    void retrySaveReportsWhetherItSucceeded() {
        FakeGameState state = new FakeGameState();
        state.failSave = true;
        CapturingPresenter presenter = new CapturingPresenter();
        RestInteractor interactor = new RestInteractor(state, weatherOf(WeatherCondition.CLEAR), presenter);
        interactor.execute();

        interactor.retrySave();
        assertFalse(presenter.retryResult);

        state.failSave = false;
        interactor.retrySave();
        assertTrue(presenter.retryResult);
        assertEquals(2, state.savedDayNumber);
    }

    @Test
    void restIsRefusedWhileAnActivityIsInProgress() {
        FakeGameState state = new FakeGameState();
        state.player.startActivity("attempt-1");
        CapturingPresenter presenter = new CapturingPresenter();

        new RestInteractor(state, weatherOf(WeatherCondition.CLEAR), presenter).execute();

        assertEquals(RestInteractor.ACTIVITY_IN_PROGRESS_MESSAGE, presenter.failMessage);
        assertNull(presenter.output);
        assertEquals(1, state.getCurrentDay().getDayNumber());
        assertEquals(20, state.getPlayer().getEnergy());
        assertEquals(0, state.saveCount);
    }
}
