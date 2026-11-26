package interface_adapter.view_history;

import interface_adapter.ViewModel;

/**
 * The view model for the communication history screen.
 */
public class ViewHistoryViewModel extends ViewModel<ViewHistoryState> {

    public static final String VIEW_NAME = "history";

    public ViewHistoryViewModel() {
        super(VIEW_NAME);
        setState(new ViewHistoryState());
    }
}