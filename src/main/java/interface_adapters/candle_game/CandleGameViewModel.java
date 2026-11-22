package interface_adapters.candle_game;

import entities.Game.CandleType;
import entities.Game.GameCondition;
import entities.Game.Guess;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Signup View.
 */

public class CandleGameViewModel {

    private List<CandleType> candleFeedback;
    private int attemptsRemaining;
    private GameCondition gameCondition;
    private List<Guess> pastGuesses;
    private final List<CandleGameViewModelListener> listeners = new ArrayList<>();

    public List<CandleType> getCandleFeedback() {
        return candleFeedback;
    }

    public void setCandleFeedback(List<CandleType> candleFeedback) {
        this.candleFeedback = candleFeedback;
        notifyStateChanged();
    }

    public List<Guess> getPastGuesses() {
        return pastGuesses;
    }
    public void setPastGuesses(List<Guess> pastGuesses) {
        this.pastGuesses = pastGuesses;
        notifyStateChanged();
    }

    public int getAttemptsRemaining(){
        return attemptsRemaining;
    }
    public void setAttemptsRemaining(int attemptsRemaining) {
        this.attemptsRemaining = attemptsRemaining;
        notifyStateChanged();
    }

    public GameCondition getGameCondition() {
        return gameCondition;
    }

    public void setGameCondition(GameCondition gameCondition) {
        this.gameCondition = gameCondition;
        notifyStateChanged();
    }

    /**
     * Registers a listener to be notified when the Evaluate Guess View Model changes.
     * @param listener a View
     */

    public void addListener(CandleGameViewModelListener listener) {
        listeners.add(listener);
    }

    private void notifyStateChanged() {
        for (CandleGameViewModelListener listener : listeners) {
            listener.candleGameStateChanged();
        }
    }

}
