package interface_adapters.candle_game;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Signup View.
 */

public class CandleGameViewModel {

    private String[] candleFeedback;
    private int attemptsRemaining;
    private String gameCondition;

    private final List<CandleGameViewModelListener> listeners = new ArrayList<>();

    public void addListener(CandleGameViewModelListener listener) {
        listeners.add(listener);
    }

    public void fireCandleGameStateChanged() {
        for (CandleGameViewModelListener listener : listeners) {
            listener.candleGameStateChanged();
        }
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }
}
