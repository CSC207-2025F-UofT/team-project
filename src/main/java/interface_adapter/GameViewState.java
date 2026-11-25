package interface_adapter;

import java.util.Objects;
import use_case.GameOutputData;

/** One complete render value. */
public final class GameViewState {
    private final GameOutputData data;
    private final boolean busy;

    public GameViewState(GameOutputData data, boolean busy) {
        this.data = Objects.requireNonNull(data, "data");
        this.busy = busy;
    }

    public GameOutputData data() { return data; }
    public boolean busy() { return busy; }
}
