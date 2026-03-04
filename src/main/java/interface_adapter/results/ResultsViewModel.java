package interface_adapter.results;

import interface_adapter.ViewModel;

/**
 * ViewModel for the results screen.
 */
public class ResultsViewModel extends ViewModel<ResultsState> {
    public static final String TITLE_LABEL = "Game Results";
    public static final String BACK_BUTTON_LABEL = "Play Again";

    public ResultsViewModel() {
        super("results");
        setState(new ResultsState());
    }
}
