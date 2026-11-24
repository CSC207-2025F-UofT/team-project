package interface_adapter.persistence;

import interface_adapter.ViewModel;
import use_case.SavedGameSummary;

import java.util.List;

public final class PersistenceViewModel extends ViewModel<PersistenceViewModel.State> {
    public static final String REQUEST_NAME = "requestName";
    public static final String SAVES_UPDATED = "savesUpdated";
    public static final String GAME_LOADED = "gameLoaded";

    private List<SavedGameSummary> saves = List.of();

    public record State(boolean busy, String message) {
    }

    public PersistenceViewModel() {
        super("persistence");
        setState(new State(false, ""));
    }

    public List<SavedGameSummary> getSaves() {
        return saves;
    }

    public void setSaves(List<SavedGameSummary> saves) {
        this.saves = List.copyOf(saves);
    }
}
