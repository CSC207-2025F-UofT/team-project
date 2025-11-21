package interface_adapter.browselandmarks;

import interface_adapter.ViewModel;

public class BrowseLandmarksViewModel extends ViewModel<BrowseLandmarksState> {
    public BrowseLandmarksViewModel() {
        super("browse landmarks");  // view name = "browse landmarks"
        setState(new BrowseLandmarksState());
    }
}