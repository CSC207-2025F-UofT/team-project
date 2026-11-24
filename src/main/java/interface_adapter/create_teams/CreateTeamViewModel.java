package interface_adapter.create_teams;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Game Running View.
 */
public class CreateTeamViewModel {
    private final List<CreateTeamViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "main menu";

    private CreateTeamState state = new CreateTeamState();

    public CreateTeamState getState() {
        return state;
    }

    public void setState(CreateTeamState state) {
        this.state = state;
    }

    public void addListener(CreateTeamViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (CreateTeamViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
