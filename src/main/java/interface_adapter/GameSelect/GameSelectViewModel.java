package interface_adapter.GameSelect;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class GameSelectViewModel extends ViewModel<GameSelectState> {

    public GameSelectViewModel() {
        super("game select");
        setState(new GameSelectState());
    }

}
