package interface_adapter.view_closet;
// view model holds that state and provides methods to notify the screen when it changes

import interface_adapter.ViewModel;
import interface_adapter.ViewNames;

public class ViewClosetViewModel extends ViewModel<ViewClosetState> {

    public ViewClosetViewModel() {
        super(ViewNames.CLOSET);
        setState(new ViewClosetState());
    }
}